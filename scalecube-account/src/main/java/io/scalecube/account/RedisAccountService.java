package io.scalecube.account;

import static com.google.common.base.Preconditions.checkNotNull;

import io.scalecube.account.api.AccountService;
import io.scalecube.account.api.AddOrganizationApiKeyRequest;
import io.scalecube.account.api.ApiKey;
import io.scalecube.account.api.CreateOrganizationRequest;
import io.scalecube.account.api.CreateOrganizationResponse;
import io.scalecube.account.api.DeleteOrganizationApiKeyRequest;
import io.scalecube.account.api.DeleteOrganizationRequest;
import io.scalecube.account.api.DeleteOrganizationResponse;
import io.scalecube.account.api.EmailNotVerifiedException;
import io.scalecube.account.api.FindUserRequest;
import io.scalecube.account.api.FindUserResponse;
import io.scalecube.account.api.GetMembershipRequest;
import io.scalecube.account.api.GetMembershipResponse;
import io.scalecube.account.api.GetOrganizationMembersRequest;
import io.scalecube.account.api.GetOrganizationMembersResponse;
import io.scalecube.account.api.GetOrganizationRequest;
import io.scalecube.account.api.GetOrganizationResponse;
import io.scalecube.account.api.InvalidAuthenticationToken;
import io.scalecube.account.api.InvalidRequestException;
import io.scalecube.account.api.InviteOrganizationMemberRequest;
import io.scalecube.account.api.InviteOrganizationMemberResponse;
import io.scalecube.account.api.KickoutOrganizationMemberRequest;
import io.scalecube.account.api.KickoutOrganizationMemberResponse;
import io.scalecube.account.api.LeaveOrganizationRequest;
import io.scalecube.account.api.LeaveOrganizationResponse;
import io.scalecube.account.api.MissingOrganizationException;
import io.scalecube.account.api.NoSuchOrganizationFound;
import io.scalecube.account.api.Organization;
import io.scalecube.account.api.OrganizationInfo;
import io.scalecube.account.api.OrganizationMember;
import io.scalecube.account.api.Token;
import io.scalecube.account.api.UpdateOrganizationRequest;
import io.scalecube.account.api.UpdateOrganizationResponse;
import io.scalecube.account.api.User;
import io.scalecube.account.db.RedisOrganizations;
import io.scalecube.account.tokens.IdGenerator;
import io.scalecube.account.tokens.JwtApiKey;
import io.scalecube.account.tokens.MockTokenVerification;
import io.scalecube.account.tokens.TokenVerification;
import io.scalecube.account.tokens.TokenVerifier;

import com.google.common.collect.Lists;

import org.redisson.api.RedissonClient;

import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RedisAccountService implements AccountService {

  private final RedisOrganizations accountManager;

  private final TokenVerifier tokenVerifier;

  public static class Builder {

    private RedissonClient redisson;
    private User user;

    public Builder redisson(RedissonClient redisson) {
      this.redisson = redisson;
      return this;
    }

    public Builder mock(User user) {
      this.user = user;
      return this;
    }

    /**
     * build a Service instance.
     * 
     * @return new initialzed service instance.
     */
    public RedisAccountService build() {
      TokenVerifier tokenVerifier = null;
      if (user == null) {
        tokenVerifier = new TokenVerification(new RedisOrganizations(redisson));
      } else {
        tokenVerifier = new MockTokenVerification(user);
      }
      return new RedisAccountService(new RedisOrganizations(redisson), tokenVerifier);
    }

  }

  public static Builder builder() {
    return new Builder();
  }

  private RedisAccountService(RedisOrganizations accountManager, TokenVerifier tokenVerifier) {
    this.accountManager = accountManager;
    this.tokenVerifier = tokenVerifier;
  }

  @Override
  public Mono<User> register(final Token token) {
    return Mono.create(sink -> {
      try {
        User user = tokenVerifier.verify(token);
        if (user != null) {
          if (user.emailVerified()) {
            accountManager.register(user);
            sink.success(user);
          } else {
            sink.error(new EmailNotVerifiedException("please verify your email first"));
          }
        } else {
          sink.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        sink.error(ex);
      }
    });
  }

  @Override
  public Mono<FindUserResponse> searchUser(FindUserRequest request) {
    checkNotNull(request);
    checkNotNull(request.fullNameOrEmail());

    return Mono.create(sink -> {
      if (request.fullNameOrEmail().length() >= 3) {
        sink.success(new FindUserResponse(accountManager.searchUserByUserNameOrEmail(request.fullNameOrEmail())));
      } else {
        sink.success(new FindUserResponse());
      }
    });
  }

  @Override
  public Mono<CreateOrganizationResponse> createOrganization(CreateOrganizationRequest request) {
    checkNotNull(request);
    return Mono.create(sink -> {
      try {
        final User user = tokenVerifier.verify(request.token());
        if (user != null && isKnownUser(user)) {
          final String secretKey = IdGenerator.generateId();

          final Organization newOrg = accountManager.createOrganization(user, Organization.builder()
              .id(IdGenerator.generateId())
              .name(request.name())
              .ownerId(user.id())
              .email(request.email())
              .secretKey(secretKey)
              .build());

          sink.success(new CreateOrganizationResponse(newOrg.id(),
              newOrg.name(),
              newOrg.apiKeys(),
              newOrg.email(),
              newOrg.ownerId()));

        } else {
          sink.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        sink.error(ex);
      }
    });
  }

  private boolean isKnownUser(User user) {
    return accountManager.getUser(user.id()) != null;
  }

  @Override
  public Mono<GetOrganizationResponse> addOrganizationApiKey(AddOrganizationApiKeyRequest request) {
    return Mono.create(future -> {
      try {
        checkNotNull(request);
        checkNotNull(request.organizationId(), "organizationId is a required argument");
        checkNotNull(request.token(), "token is a required argument");
        checkNotNull(request.apiKeyName(), "apiKeyName is a required argument");

        final User user = tokenVerifier.verify(request.token());
        if (user != null && isKnownUser(user)) {
          final Organization org = accountManager.getOrganization(request.organizationId());

          ApiKey apiKey = JwtApiKey.builder().origin("account-service")
              .subject(org.id())
              .name(request.apiKeyName())
              .claims(request.claims())
              .id(org.id())
              .build(org.secretKey());

          JwtApiKey[] apiKeys = Lists.asList(apiKey, org.apiKeys()).toArray(new JwtApiKey[org.apiKeys().length + 1]);

          Organization newOrg = org.builder().apiKey(apiKeys).copy(org);

          accountManager.updateOrganizationDetails(user, org, newOrg);

          future.success(new GetOrganizationResponse(newOrg.id(), newOrg.name(), newOrg.apiKeys(), newOrg.email(),
              newOrg.ownerId()));

        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Throwable ex) {
        ex.printStackTrace();
        future.error(ex);
      }
    });
  }

  @Override
  public Mono<GetOrganizationResponse> deleteOrganizationApiKey(DeleteOrganizationApiKeyRequest request) {
    checkNotNull(request);
    checkNotNull(request.organizationId());
    checkNotNull(request.token());
    checkNotNull(request.apiKeyName());

    return Mono.create(future -> {
      try {
        final User user = tokenVerifier.verify(request.token());
        if (user != null && isKnownUser(user)) {
          final Organization org = accountManager.getOrganization(request.organizationId());

          List<ApiKey> apiKeys = Arrays.asList(org.apiKeys());

          List<ApiKey> reduced = apiKeys.stream()
              .filter(api -> !api.name().equals(request.apiKeyName()))
              .collect(Collectors.toList());

          Organization newOrg = org.builder().apiKey(reduced.toArray(new ApiKey[reduced.size()])).copy(org);

          accountManager.updateOrganizationDetails(user, org, newOrg);

          future.success(new GetOrganizationResponse(newOrg.id(), newOrg.name(), newOrg.apiKeys(), newOrg.email(),
              newOrg.ownerId()));

        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        future.error(ex);
      }
    });
  }

  @Override
  public Mono<DeleteOrganizationResponse> deleteOrganization(DeleteOrganizationRequest request) {
    checkNotNull(request);
    checkNotNull(request.organizationId());
    checkNotNull(request.token());

    return Mono.create(future -> {
      try {
        final User user = tokenVerifier.verify(request.token());
        if (user != null && isKnownUser(user)) {
          final Organization org = accountManager.getOrganization(request.organizationId());
          if (org != null) {
            accountManager.deleteOrganization(user, org);
            future.success(new DeleteOrganizationResponse(org.id(), true));
          } else {
            future.error(new NoSuchOrganizationFound(request.organizationId()));
          }
        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        future.error(ex);
      }
    });
  }

  @Override
  public Mono<UpdateOrganizationResponse> updateOrganization(UpdateOrganizationRequest request) {
    checkNotNull(request);
    checkNotNull(request.organizationId());
    checkNotNull(request.token());

    return Mono.create(future -> {
      try {
        final User owner = tokenVerifier.verify(request.token());
        if (owner != null && isKnownUser(owner)) {
          final Organization org = accountManager.getOrganization(request.organizationId());
          if (org != null) {

            Organization newDetails = Organization.builder()
                .name(request.name())
                .email(request.email())
                .copy(org);

            accountManager.updateOrganizationDetails(owner, org, newDetails);
            future.success(new UpdateOrganizationResponse(newDetails.id(), newDetails.name(), newDetails.apiKeys(),
                newDetails.email(), newDetails.ownerId()));
          } else {
            future.error(new NoSuchOrganizationFound(request.organizationId()));
          }
        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        future.error(ex);
      }
    });
  }

  @Override
  public Mono<GetMembershipResponse> getUserOrganizationsMembership(GetMembershipRequest request) {
    checkNotNull(request);
    checkNotNull(request.token());

    return Mono.create(future -> {

      Collection<Organization> results = new ArrayList<>();
      try {
        User user = tokenVerifier.verify(request.token());
        if (user != null && isKnownUser(user)) {
          results = accountManager.getUserMembership(user);

          final List<OrganizationInfo> infos = new ArrayList<>();
          results.forEach(item -> {
            infos.add(new OrganizationInfo(item.id(), item.name(), item.apiKeys(), item.email(), item.ownerId()));
          });

          future.success(new GetMembershipResponse(infos.toArray(new OrganizationInfo[results.size()])));
        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        future.error(ex);
      }

    });

  }

  @Override
  public Mono<GetOrganizationResponse> getOrganization(GetOrganizationRequest request) {
    checkNotNull(request);
    checkNotNull(request.organizationId());
    checkNotNull(request.token());

    return Mono.create(future -> {

      Organization result = null;
      try {
        User user = tokenVerifier.verify(request.token());
        if (user != null && isKnownUser(user)) {
          result = accountManager.getOrganization(request.organizationId());
          if (result != null) {
            future.success(
                new GetOrganizationResponse(result.id(), result.name(), result.apiKeys(), result.email(),
                    result.ownerId()));
          } else {
            future.error(new MissingOrganizationException(request.organizationId()));
          }
        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        future.error(ex);
      }

    });

  }


  /////////////////////////////
  // MEMBERSHIP
  ////////////////////////////

  @Override
  public Mono<GetOrganizationMembersResponse> getOrganizationMembers(
      GetOrganizationMembersRequest request) {
    checkNotNull(request);
    checkNotNull(request.organizationId());
    checkNotNull(request.token());

    return Mono.create(future -> {

      Collection<OrganizationMember> result = null;
      try {
        User user = tokenVerifier.verify(request.token());
        if (user != null && isKnownUser(user)) {
          result = accountManager.getOrganizationMembers(request.organizationId());
          future.success(
              new GetOrganizationMembersResponse(
                  (OrganizationMember[]) result.toArray(new OrganizationMember[result.size()])));
        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        future.error(ex);
      }

    });

  }

  @Override
  public Mono<InviteOrganizationMemberResponse> inviteMember(InviteOrganizationMemberRequest request) {
    return Mono.create(future -> {

      try {
        User owner = tokenVerifier.verify(request.token());
        if (owner != null && isKnownUser(owner)) {
          Organization organization = accountManager.getOrganization(request.organizationId());
          User user = accountManager.getUser(request.userId());
          if (organization != null && user != null) {
            accountManager.invite(owner, organization, user);
            future.success(new InviteOrganizationMemberResponse());
          } else {
            future.error(new InvalidRequestException(
                "Cannot success request, target-organization or target-user was not found."));
          }
        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        future.error(ex);
      }

    });

  }

  @Override
  public Mono<KickoutOrganizationMemberResponse> kickoutMember(KickoutOrganizationMemberRequest request) {
    checkNotNull(request);
    checkNotNull(request.organizationId());
    checkNotNull(request.token());
    checkNotNull(request.userId());
    return Mono.create(future -> {

      try {
        User owner = tokenVerifier.verify(request.token());
        if (owner != null && isKnownUser(owner)) {
          Organization organization = accountManager.getOrganization(request.organizationId());
          User user = accountManager.getUser(request.userId());
          if (organization != null && user != null) {
            accountManager.kickout(owner, organization, user);
            future.success(new KickoutOrganizationMemberResponse());
          } else {
            future.error(new InvalidRequestException(
                "Cannot success request, target-organization or target-user was not found."));
          }
        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        future.error(ex);
      }

    });

  }

  @Override
  public Mono<LeaveOrganizationResponse> leaveOrganization(LeaveOrganizationRequest request) {
    checkNotNull(request);
    checkNotNull(request.organizationId());
    checkNotNull(request.token());

    return Mono.create(future -> {

      try {
        User user = tokenVerifier.verify(request.token());
        if (user != null && isKnownUser(user)) {
          Organization organization = accountManager.getOrganization(request.organizationId());
          if (organization != null) {
            accountManager.leave(organization, user);
            future.success(new LeaveOrganizationResponse());
          } else {
            future.error(new InvalidRequestException(
                "Cannot success request, target-organization was not found."));
          }
        } else {
          future.error(new InvalidAuthenticationToken());
        }
      } catch (Exception ex) {
        future.error(ex);
      }

    });
  }
}

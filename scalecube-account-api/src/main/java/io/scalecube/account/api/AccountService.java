package io.scalecube.account.api;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;

import reactor.core.publisher.Mono;

@Service
public interface AccountService {

  @ServiceMethod
  public Mono<User> register(final Token token);

  @ServiceMethod
  public Mono<CreateOrganizationResponse> createOrganization(CreateOrganizationRequest request);

  @ServiceMethod
  public Mono<GetMembershipResponse> getUserOrganizationsMembership(GetMembershipRequest request);

  @ServiceMethod
  public Mono<GetOrganizationResponse> getOrganization(GetOrganizationRequest request);

  @ServiceMethod
  public Mono<DeleteOrganizationResponse> deleteOrganization(DeleteOrganizationRequest request);

  @ServiceMethod
  public Mono<UpdateOrganizationResponse> updateOrganization(UpdateOrganizationRequest request);

  @ServiceMethod
  public Mono<GetOrganizationMembersResponse> getOrganizationMembers(
      GetOrganizationMembersRequest request);

  @ServiceMethod
  public Mono<InviteOrganizationMemberResponse> inviteMember(InviteOrganizationMemberRequest request);

  @ServiceMethod
  public Mono<KickoutOrganizationMemberResponse> kickoutMember(KickoutOrganizationMemberRequest request);

  @ServiceMethod
  public Mono<LeaveOrganizationResponse> leaveOrganization(LeaveOrganizationRequest request);

  @ServiceMethod
  public Mono<GetOrganizationResponse> addOrganizationApiKey(AddOrganizationApiKeyRequest request);

  @ServiceMethod
  public Mono<GetOrganizationResponse> deleteOrganizationApiKey(DeleteOrganizationApiKeyRequest request);

  @ServiceMethod
  public Mono<FindUserResponse> searchUser(FindUserRequest request);


}

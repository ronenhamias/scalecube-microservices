package io.scalecube.account.api;

import java.util.concurrent.CompletableFuture;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;

@Service
public interface AccountService {

  @ServiceMethod
  public CompletableFuture<User> register(final Token token);

  @ServiceMethod
  public CompletableFuture<CreateOrganizationResponse> createOrganization(CreateOrganizationRequest request);

  @ServiceMethod
  public CompletableFuture<GetMembershipResponse> getUserOrganizationsMembership(GetMembershipRequest request);

  @ServiceMethod
  public CompletableFuture<GetOrganizationResponse> getOrganization(GetOrganizationRequest request);

  @ServiceMethod
  public CompletableFuture<DeleteOrganizationResponse> deleteOrganization(DeleteOrganizationRequest request);

  @ServiceMethod
  public CompletableFuture<UpdateOrganizationResponse> updateOrganization(UpdateOrganizationRequest request);

  @ServiceMethod
  public CompletableFuture<GetOrganizationMembersResponse> getOrganizationMembers(
      GetOrganizationMembersRequest request);

  @ServiceMethod
  public CompletableFuture<InviteOrganizationMemberResponse> inviteMember(InviteOrganizationMemberRequest request);

  @ServiceMethod
  public CompletableFuture<KickoutOrganizationMemberResponse> kickoutMember(KickoutOrganizationMemberRequest request);

  @ServiceMethod
  public CompletableFuture<LeaveOrganizationResponse> leaveOrganization(LeaveOrganizationRequest request);

  @ServiceMethod
  public CompletableFuture<GetOrganizationResponse> addOrganizationApiKey(AddOrganizationApiKeyRequest request);

  @ServiceMethod
  public CompletableFuture<GetOrganizationResponse> deleteOrganizationApiKey(DeleteOrganizationApiKeyRequest request);

  @ServiceMethod
  public CompletableFuture<FindUserResponse> searchUser(FindUserRequest request);


}

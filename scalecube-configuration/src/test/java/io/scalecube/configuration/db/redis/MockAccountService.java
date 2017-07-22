package io.scalecube.configuration.db.redis;

import io.scalecube.account.api.AccountService;
import io.scalecube.account.api.AddOrganizationApiKeyRequest;
import io.scalecube.account.api.CreateOrganizationRequest;
import io.scalecube.account.api.CreateOrganizationResponse;
import io.scalecube.account.api.DeleteOrganizationApiKeyRequest;
import io.scalecube.account.api.DeleteOrganizationRequest;
import io.scalecube.account.api.DeleteOrganizationResponse;
import io.scalecube.account.api.FindUserRequest;
import io.scalecube.account.api.FindUserResponse;
import io.scalecube.account.api.GetMembershipRequest;
import io.scalecube.account.api.GetMembershipResponse;
import io.scalecube.account.api.GetOrganizationMembersRequest;
import io.scalecube.account.api.GetOrganizationMembersResponse;
import io.scalecube.account.api.GetOrganizationRequest;
import io.scalecube.account.api.GetOrganizationResponse;
import io.scalecube.account.api.InviteOrganizationMemberRequest;
import io.scalecube.account.api.InviteOrganizationMemberResponse;
import io.scalecube.account.api.KickoutOrganizationMemberRequest;
import io.scalecube.account.api.KickoutOrganizationMemberResponse;
import io.scalecube.account.api.LeaveOrganizationRequest;
import io.scalecube.account.api.LeaveOrganizationResponse;
import io.scalecube.account.api.Token;
import io.scalecube.account.api.UpdateOrganizationRequest;
import io.scalecube.account.api.UpdateOrganizationResponse;
import io.scalecube.account.api.User;

import java.util.concurrent.CompletableFuture;

public class MockAccountService implements AccountService {

  private final User user;

  public MockAccountService(User user){
    this.user= user;
  }
  
  @Override
  public CompletableFuture<User> register(Token token) {
    return CompletableFuture.completedFuture(this.user);
  }

  @Override
  public CompletableFuture<CreateOrganizationResponse> createOrganization(CreateOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<GetMembershipResponse> getUserOrganizationsMembership(GetMembershipRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<GetOrganizationResponse> getOrganization(GetOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<DeleteOrganizationResponse> deleteOrganization(DeleteOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<UpdateOrganizationResponse> updateOrganization(UpdateOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<GetOrganizationMembersResponse> getOrganizationMembers(
      GetOrganizationMembersRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<InviteOrganizationMemberResponse> inviteMember(InviteOrganizationMemberRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<KickoutOrganizationMemberResponse> kickoutMember(KickoutOrganizationMemberRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<LeaveOrganizationResponse> leaveOrganization(LeaveOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<GetOrganizationResponse> addOrganizationApiKey(AddOrganizationApiKeyRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<GetOrganizationResponse> deleteOrganizationApiKey(DeleteOrganizationApiKeyRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<FindUserResponse> searchUser(FindUserRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

}

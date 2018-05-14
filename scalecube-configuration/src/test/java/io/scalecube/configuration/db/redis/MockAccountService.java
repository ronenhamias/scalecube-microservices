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

import reactor.core.publisher.Mono;

public class MockAccountService implements AccountService {

  private final User user;

  public MockAccountService(User user){
    this.user= user;
  }
  
  @Override
  public Mono<User> register(Token token) {
    return Mono.just(this.user);
  }

  @Override
  public Mono<CreateOrganizationResponse> createOrganization(CreateOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<GetMembershipResponse> getUserOrganizationsMembership(GetMembershipRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<GetOrganizationResponse> getOrganization(GetOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<DeleteOrganizationResponse> deleteOrganization(DeleteOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<UpdateOrganizationResponse> updateOrganization(UpdateOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<GetOrganizationMembersResponse> getOrganizationMembers(
      GetOrganizationMembersRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<InviteOrganizationMemberResponse> inviteMember(InviteOrganizationMemberRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<KickoutOrganizationMemberResponse> kickoutMember(KickoutOrganizationMemberRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<LeaveOrganizationResponse> leaveOrganization(LeaveOrganizationRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<GetOrganizationResponse> addOrganizationApiKey(AddOrganizationApiKeyRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<GetOrganizationResponse> deleteOrganizationApiKey(DeleteOrganizationApiKeyRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mono<FindUserResponse> searchUser(FindUserRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

}

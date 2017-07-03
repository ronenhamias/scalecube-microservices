package io.scalecube.account.api;

public class GetOrganizationMembersResponse {

  private OrganizationMember[] members;

  public GetOrganizationMembersResponse(OrganizationMember[] members) {
    this.members = members;
  }

}

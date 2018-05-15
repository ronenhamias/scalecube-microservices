package io.scalecube.account.api;

public class GetOrganizationMembersResponse {

  private OrganizationMember[] members;

  /**
   * @deprecated only for serialization/deserialization
   */
  GetOrganizationMembersResponse() {}

  public GetOrganizationMembersResponse(OrganizationMember[] members) {
    this.members = members;
  }

  public OrganizationMember[] members() {
    return this.members;
  }
}

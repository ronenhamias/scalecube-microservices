package io.scalecube.account.api;

public class GetOrganizationMembersRequest {

  private String organizationId;

  private Token token;

  public Token token() {
    return this.token;
  }

  public String organizationId() {
    return this.organizationId;
  }

}

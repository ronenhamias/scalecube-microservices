package io.scalecube.account.api;

public class KickoutOrganizationMemberRequest {

  private Token token;
  private String organizationId;
  private String userId;

  public Token token() {
    return this.token;
  }

  public String organizationId() {
    return this.organizationId;
  }

  public String userId() {
    return this.userId;
  }

}

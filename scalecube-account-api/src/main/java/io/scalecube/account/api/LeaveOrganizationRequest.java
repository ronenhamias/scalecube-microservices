package io.scalecube.account.api;

public class LeaveOrganizationRequest {

  private Token token;

  private String organizationId;

  public Token token() {
    return this.token;
  }

  public String organizationId() {
    return this.organizationId;
  }

}

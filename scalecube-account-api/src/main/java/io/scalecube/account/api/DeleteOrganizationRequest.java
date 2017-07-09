package io.scalecube.account.api;

public class DeleteOrganizationRequest {

  private String organizationId;

  private Token token;

  public Token token() {
    return this.token;
  }

  public String organizationId() {
    return this.organizationId;
  }
}

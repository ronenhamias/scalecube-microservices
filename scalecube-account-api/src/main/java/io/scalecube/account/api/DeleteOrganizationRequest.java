package io.scalecube.account.api;

public class DeleteOrganizationRequest {

  private String organizationId;

  private Token token;

  public DeleteOrganizationRequest(Token token, String organizationId) {
    this.token = token;
    this.organizationId = organizationId;
  }

  public Token token() {
    return this.token;
  }

  public String organizationId() {
    return this.organizationId;
  }
}

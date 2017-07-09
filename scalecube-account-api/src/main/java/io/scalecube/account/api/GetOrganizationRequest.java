package io.scalecube.account.api;

public class GetOrganizationRequest {

  private Token token;

  private String organizationId;

  public GetOrganizationRequest() {}

  public GetOrganizationRequest(Token token, String organizationId) {
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

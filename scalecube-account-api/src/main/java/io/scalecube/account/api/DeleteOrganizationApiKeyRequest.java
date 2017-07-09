package io.scalecube.account.api;

public class DeleteOrganizationApiKeyRequest {

  private Token token;
  private String organizationId;
  private String apiKeyName;

  public Token token() {
    return this.token;
  }

  public String organizationId() {
    return this.organizationId;
  }

  public String apiKeyName() {
    return this.apiKeyName;
  }

}

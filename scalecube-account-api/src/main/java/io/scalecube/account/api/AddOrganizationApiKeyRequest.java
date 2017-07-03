package io.scalecube.account.api;

import java.util.Map;

public class AddOrganizationApiKeyRequest {

  private Token token;
  private String organizationId;
  private String apiKeyName;
  private Map<String, String> claims;

  public Token token() {
    return this.token;
  }

  public String organizationId() {
    return this.organizationId;
  }

  public String apiKeyName() {
    return this.apiKeyName;
  }

  public Map<String, String> claims() {
    return this.claims;
  }

}

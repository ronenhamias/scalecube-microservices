package io.scalecube.account.api;

public class UpdateOrganizationRequest {

  private Token token;

  private String name;

  private String email;

  private String organizationId;

  public String name() {
    return this.name;
  }

  public String email() {
    return this.email;
  }

  public Token token() {
    return this.token;
  }

  public String organizationId() {
    return this.organizationId;
  }
}

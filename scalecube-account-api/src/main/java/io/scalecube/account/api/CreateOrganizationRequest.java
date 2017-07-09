package io.scalecube.account.api;

public class CreateOrganizationRequest {

  private String name;

  private Token token;

  private String email;

  public CreateOrganizationRequest() {}

  public CreateOrganizationRequest(String name, Token token, String email) {
    this.name = name;
    this.token = token;
    this.email = email;
  }

  public String name() {
    return name;
  }

  public Token token() {
    return token;
  }

  public String email() {
    return email;
  }
}

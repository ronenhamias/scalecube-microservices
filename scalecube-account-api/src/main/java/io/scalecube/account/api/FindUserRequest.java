package io.scalecube.account.api;

public class FindUserRequest {

  private Token token;

  private String fullNameOrEmail;

  public String fullNameOrEmail() {
    return this.fullNameOrEmail;
  }

  public Token token() {
    return this.token;
  }
}

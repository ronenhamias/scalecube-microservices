package io.scalecube.account.api;

public class GetMembershipRequest {

  private Token token;

  public GetMembershipRequest() {}

  public GetMembershipRequest(Token token) {
    this.token = token;
  }

  public Token token() {
    return this.token;
  }
}

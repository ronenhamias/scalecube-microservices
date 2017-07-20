package io.scalecube.account.tokens;

import io.scalecube.account.api.Token;
import io.scalecube.account.api.User;

public class MockTokenVerification implements TokenVerifier {

  private User user;

  public MockTokenVerification(User user) {
    this.user = user;
  }

  @Override
  public User verify(Token token) throws Exception {
    return user;
  }

}


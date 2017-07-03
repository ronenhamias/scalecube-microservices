package io.scalecube.account.tokens;

import io.scalecube.account.api.Token;
import io.scalecube.account.api.User;

public interface TokenVerifier {

  public User verify(Token token) throws Exception;

}

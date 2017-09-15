package io.scalecube.account.api;

public class Token {

  @Override
  public String toString() {
    return "Token [origin=" + origin + ", token=" + token + "]";
  }

  private String origin;

  private String token;

  public Token() {}

  /**
   * Token information.
   * 
   * @param origin where this token was provided.
   * @param token the jwt token string.
   */
  public Token(String origin, String token) {
    this.token = token;
    this.origin = origin;
  }

  public String token() {
    return this.token;
  }

  /**
   * source for this token for example: google, twitter, github.
   * 
   * @return origin of the token.
   */
  public String origin() {
    return this.origin;
  }
}

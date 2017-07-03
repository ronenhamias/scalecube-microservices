package io.scalecube.account.api;

public class Token {

  private String origin;

  private String token;

  public Token(){};
  
  public Token(String origin, String token) {
    this.token = token;
    this.origin=origin;
  }

  public String token() {
    return this.token;
  }

  /**
   * source for this token for example: google, twitter, github
   * 
   * @return
   */
  public String origin() {
    return this.origin;
  }
}

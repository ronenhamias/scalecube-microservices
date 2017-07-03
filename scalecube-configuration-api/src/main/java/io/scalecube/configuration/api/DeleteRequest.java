package io.scalecube.configuration.api;

import io.scalecube.account.api.Token;

public class DeleteRequest implements AccessRequest {

  private Token token;
  
  private String collection;
  
  private String key;
  
  
  public Token token() {  
    return token;
  }

  public String collection() {
    return collection;
  }

  public String key() {
    return key;
  }

}

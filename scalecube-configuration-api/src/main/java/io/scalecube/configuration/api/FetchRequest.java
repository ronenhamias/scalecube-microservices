package io.scalecube.configuration.api;

import io.scalecube.account.api.Token;

public class FetchRequest implements AccessRequest {

  protected String collection;

  protected String key;

  private Token token;

  public FetchRequest() {}

  public FetchRequest(String collection, String key) {
    this.collection = collection;
    this.key = key;
  }

  public FetchRequest(Token token, String collection, String key) {
    this.token = token;
    this.collection = collection;
    this.key = key;
  }

  public String collection() {
    return collection;
  }

  public String key() {
    return key;
  }

  public Token token() {
    return this.token;
  }

}

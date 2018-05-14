package io.scalecube.configuration.api;

import io.scalecube.account.api.Token;

public class DeleteRequest implements AccessRequest {

  private Token token;
  private String collection;
  private String key;

  /**
   * @deprecated only for serialization/deserialization
   */
  DeleteRequest() {}

  public DeleteRequest(Token token, String collection, String key) {
    this.token = token;
    this.collection = collection;
    this.key = key;
  }

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

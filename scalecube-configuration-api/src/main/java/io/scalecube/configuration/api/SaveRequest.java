package io.scalecube.configuration.api;

import io.scalecube.account.api.Token;

import com.fasterxml.jackson.databind.JsonNode;

public class SaveRequest implements AccessRequest {

  private Token token;
  private String collection;
  private String key;
  private JsonNode value;

  /**
   * @deprecated only for serialization/deserialization
   */
  SaveRequest() {}

  public SaveRequest(String collection, String key, JsonNode value) {
    this.collection = collection;
    this.key = key;
    this.value = value;
  }

  public SaveRequest(Token token, String collection, String key, JsonNode value) {
    this.token = token;
    this.collection = collection;
    this.key = key;
    this.value = value;
  }

  public Token token() {
    return this.token;
  }

  public JsonNode value() {
    return this.value;
  }

  public String key() {
    return this.key;
  }

  public String collection() {
    return this.collection;
  }
}

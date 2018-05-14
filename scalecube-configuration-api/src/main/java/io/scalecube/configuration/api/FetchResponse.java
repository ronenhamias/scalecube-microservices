package io.scalecube.configuration.api;

public class FetchResponse {

  private Object value;
  private String key;

  public static Builder builder() {
    return new Builder();
  }

  /**
   * @deprecated only for serialization/deserialization
   */
  FetchResponse() {}

  public FetchResponse(String key, Object value) {
    this.value = value;
    this.key = key;
  }

  public Object value() {
    return this.value;
  }

  @Override
  public String toString() {
    return "FetchResponse [value=" + value + ", key=" + key + "]";
  }

  public static class Builder {
    private Object value;
    private String key;

    public FetchResponse build() {
      return new FetchResponse(this.key, this.value);
    }

    public Builder value(Object value) {
      this.value = value;
      return this;
    }

    public Builder key(String key) {
      this.key = key;
      return this;
    }

  }
}

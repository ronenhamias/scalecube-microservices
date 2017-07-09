package io.scalecube.account.tokens;

import io.scalecube.account.api.ApiKey;
import io.scalecube.jwt.WebToken;

import java.util.Map;

public class JwtApiKey extends ApiKey {

  public JwtApiKey() {}

  public JwtApiKey(String name, Map<String, String> claims, String apiKey) {
    super.name = name;
    super.claims = claims;
    super.key = apiKey;
  }

  public static final class Builder {

    private String origin;
    private String subject;
    private Map<String, String> claims;
    private String id;
    private String name;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder origin(String origin) {
      this.origin = origin;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder subject(String subject) {
      this.subject = subject;
      return this;
    }

    public Builder claims(Map<String, String> claims) {
      this.claims = claims;
      return this;
    }

    public ApiKey build(String secretKey) {
      final WebToken jwt = new WebToken(this.origin, this.subject);
      final String apiKey = jwt.createToken(this.id, Long.MAX_VALUE - System.currentTimeMillis(), secretKey, claims);
      return new JwtApiKey(this.name, this.claims, apiKey);
    }

  }

  public static Builder builder() {
    return new Builder();
  }

}

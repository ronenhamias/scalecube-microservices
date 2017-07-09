package io.scalecube.account.api;

import java.util.Map;

public class ApiKey {

  protected String name;
  protected Map<String, String> claims;
  protected String key;

  public String name() {
    return this.name;
  }

  public Map<String, String> claims() {
    return this.claims;
  }

  public String key() {
    return this.key;
  }
}

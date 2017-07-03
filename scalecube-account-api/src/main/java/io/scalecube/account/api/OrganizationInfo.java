package io.scalecube.account.api;

public class OrganizationInfo {


  private ApiKey[] apiKeys;
  private String id;
  private String name;
  private String email;
  private String ownerId;

  public OrganizationInfo() {}

  public OrganizationInfo(String id, String name, ApiKey[] apiKeys, String email, String ownerId) {
    this.apiKeys = apiKeys;
    this.id = id;
    this.email = email;
    this.ownerId = ownerId;
    this.name = name;
  }

  public ApiKey[] apiKeys() {
    return this.apiKeys;
  }

  public String id() {
    return this.id;
  }

  public String email() {
    return this.email;
  }

  public String name() {
    return this.name;
  }

  public String ownerId() {
    return this.ownerId;
  }


}

package io.scalecube.account.api;

/**
 * Organization information data transfer object.
 *
 */
public class OrganizationInfo {

  private ApiKey[] apiKeys;
  private String id;
  private String name;
  private String email;
  private String ownerId;

  public OrganizationInfo() {}

  /**
   * Organization Info constroctor.
   * 
   * @param id of the organization.
   * @param name of the organization
   * @param apiKeys array of the apiKeys of the organization
   * @param email of the organization
   * @param ownerId of the organization
   */
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

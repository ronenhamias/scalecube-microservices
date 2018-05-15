package io.scalecube.account.api;

public class CreateOrganizationResponse extends OrganizationInfo {

  /**
   * @deprecated only for serialization/deserialization
   */
  CreateOrganizationResponse() {}

  public CreateOrganizationResponse(String id, String name, ApiKey[] apiKey, String email, String ownerId) {
    super(id, name, apiKey, email, ownerId);
  }

}

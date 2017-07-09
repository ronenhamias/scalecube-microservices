package io.scalecube.account.api;

public class CreateOrganizationResponse extends OrganizationInfo {

  public CreateOrganizationResponse(String id, String name, ApiKey[] apiKey, String email, String ownerId) {
    super(id, name, apiKey, email, ownerId);
  }

}

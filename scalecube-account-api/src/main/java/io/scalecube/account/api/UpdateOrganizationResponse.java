package io.scalecube.account.api;

public class UpdateOrganizationResponse extends OrganizationInfo{

  public UpdateOrganizationResponse(String id, String name, ApiKey[] apiKey, String email, String ownerId) {
    super(id, name, apiKey, email, ownerId);
  }
}

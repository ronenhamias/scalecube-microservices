package io.scalecube.account.api;

public class GetOrganizationResponse extends OrganizationInfo {

  public GetOrganizationResponse() {}

  public GetOrganizationResponse(String id, String name, ApiKey[] apiKeys, String email, String ownerId) {
    super(id, name, apiKeys, email, ownerId);
  }

}

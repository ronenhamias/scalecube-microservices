package io.scalecube.account.api;

public class DeleteOrganizationResponse {

  private boolean deleted;
  private String organizationId;

  /**
   * @deprecated only for serialization/deserialization
   */
  DeleteOrganizationResponse() {}

  public DeleteOrganizationResponse(String organizationId, boolean deleted) {
    this.deleted = deleted;
    this.organizationId = organizationId;
  }

  public boolean deleted() {
    return this.deleted;
  }

  public String organizationId() {
    return this.organizationId;
  }
}

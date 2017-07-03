package io.scalecube.account.api;

import java.util.Arrays;

public class GetMembershipResponse {

  @Override
  public String toString() {
    return "GetOrganizationsResponse [organizations=" + Arrays.toString(organizations) + "]";
  }

  private OrganizationInfo[] organizations;

  public GetMembershipResponse(OrganizationInfo[] organizationInfos) {
    this.organizations= organizationInfos;
  }

}

package io.scalecube.account.api;

public class NoSuchOrganizationFound extends Throwable {

  private static final long serialVersionUID = 1L;

  public NoSuchOrganizationFound(String organizationId) {
    super("organization was found id:[" + organizationId + "]");
  }

}

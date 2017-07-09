package io.scalecube.account.api;

public class MissingOrganizationException extends Throwable {

  private static final long serialVersionUID = 1L;

  public MissingOrganizationException(String organizationId) {
    super("cannot find organization id: " + organizationId);
  }

}

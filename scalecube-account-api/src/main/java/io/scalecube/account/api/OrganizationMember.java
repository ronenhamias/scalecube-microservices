package io.scalecube.account.api;

public class OrganizationMember {

  private User user;
  private String role;

  public OrganizationMember() {}

  public OrganizationMember(User user, String role) {
    this.user = user;
    this.role = role;
  }

  public User user() {
    return this.user;
  }

  public String role() {
    return this.role;
  }
}

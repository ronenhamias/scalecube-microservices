package io.scalecube.account.api;

import java.util.Map;

public class User {

  // Get profile information from payload
  private String id;
  private String email;
  private boolean emailVerified;
  private String name;
  private String pictureUrl;
  private String locale;
  private String familyName;
  private String givenName;
  private Map<String, String> claims;

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    User other = (User) obj;

    if (getClass() != obj.getClass()) {
      return false;
    }

    if (this.id.equals(other.id())) {
      return true;
    }

    return false;
  }

  public User() {}



  /**
   * User constructor.
   * 
   * @param id of the user.
   * @param email of the user.
   * @param emailVerified of the user.
   * @param name of the user.
   * @param pictureUrl of the user.
   * @param locale of the user.
   * @param familyName of the user.
   * @param givenName of the user.
   * @param claims additional claims as key values.
   */
  public User(String id,
      String email,
      boolean emailVerified,
      String name,
      String pictureUrl,
      String locale,
      String familyName,
      String givenName,
      Map<String, String> claims) {

    this.id = id;
    this.email = email;
    this.emailVerified = emailVerified;
    this.name = name;
    this.pictureUrl = pictureUrl;
    this.locale = locale;
    this.familyName = familyName;
    this.givenName = givenName;
    this.claims = claims;
  }

  public String id() {
    return this.id;
  }

  public String email() {
    return this.email;
  }

  public boolean emailVerified() {
    return this.emailVerified;
  }

  public String name() {
    return this.name;
  }

  public String pictureUrl() {
    return this.pictureUrl;
  }

  public String locale() {
    return this.locale;
  }

  public String familyName() {
    return this.familyName;
  }

  public String givenName() {
    return this.givenName;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", email=" + email + ", emailVerified=" + emailVerified + ", name=" + name
        + ", pictureUrl=" + pictureUrl + ", locale=" + locale + ", familyName=" + familyName + ", givenName="
        + givenName + "]";
  }

  public Map<String, String> claims() {
    return this.claims;
  }
}

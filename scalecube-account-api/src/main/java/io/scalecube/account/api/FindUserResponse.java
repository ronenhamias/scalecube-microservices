package io.scalecube.account.api;

import java.util.List;

public class FindUserResponse {

  private List<User> users;

  public FindUserResponse(List<User> users) {
    this.users = users;
  }

  public FindUserResponse() {}

  public List<User> users() {
    return users;
  }

}

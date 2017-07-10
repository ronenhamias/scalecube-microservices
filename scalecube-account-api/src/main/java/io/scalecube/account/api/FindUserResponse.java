package io.scalecube.account.api;

import java.util.ArrayList;
import java.util.List;

public class FindUserResponse {

  private List<User> users = new ArrayList<>();

  public FindUserResponse(List<User> users) {
    this.users = users;
  }

  public FindUserResponse() {}

  public List<User> users() {
    return users;
  }

}

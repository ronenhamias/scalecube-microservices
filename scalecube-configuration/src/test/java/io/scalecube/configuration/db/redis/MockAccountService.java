package io.scalecube.configuration.db.redis;

import io.scalecube.account.api.AccountService;
import io.scalecube.account.api.FindUserRequest;
import io.scalecube.account.api.FindUserResponse;
import io.scalecube.account.api.Token;
import io.scalecube.account.api.User;

import reactor.core.publisher.Mono;

public class MockAccountService implements AccountService {

  private final User user;

  public MockAccountService(User user){
    this.user= user;
  }
  
  @Override
  public Mono<User> register(Token token) {
    return Mono.just(this.user);
  }

  @Override
  public Mono<FindUserResponse> searchUser(FindUserRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

}

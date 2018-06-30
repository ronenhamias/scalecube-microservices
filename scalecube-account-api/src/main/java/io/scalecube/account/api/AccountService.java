package io.scalecube.account.api;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;

import reactor.core.publisher.Mono;

@Service("accounts")
public interface AccountService {

  @ServiceMethod
  Mono<User> register(final Token token);

  @ServiceMethod
  Mono<FindUserResponse> searchUser(FindUserRequest request);

}

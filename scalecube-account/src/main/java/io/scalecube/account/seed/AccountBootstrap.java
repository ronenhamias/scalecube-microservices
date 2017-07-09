package io.scalecube.account.seed;

import io.scalecube.account.RedisAccountService;
import io.scalecube.services.Microservices;
import io.scalecube.transport.Address;

import org.redisson.Redisson;

public class AccountBootstrap {

  /**
   * AccountBootstrap main.
   * 
   * @param args appication params.
   */
  public static void main(String[] args) {

    final Microservices node = Microservices.builder()
        .seeds(Address.create("10.0.75.1", 4801))
        .services(new RedisAccountService(Redisson.create()))
        .build();

  }

}

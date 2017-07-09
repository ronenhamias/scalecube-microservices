package io.scalecube.configuration.seed;

import io.scalecube.configuration.RedisConfigurationService;
import io.scalecube.services.Microservices;

import org.redisson.Redisson;

public class ConfigurationBootstrap {

  /**
   * Main Redis configuration service bootstraper.
   * 
   * @param args application params.
   */
  public static void main(String[] args) {

    RedisConfigurationService service = new RedisConfigurationService(Redisson.create());

    Microservices ms = Microservices.builder()
        .services(service)
        .build();

    System.out.println(ms.cluster().address());
  }

}

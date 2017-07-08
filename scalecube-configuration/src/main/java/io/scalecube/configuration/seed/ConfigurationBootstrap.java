package io.scalecube.configuration.seed;

import org.redisson.Redisson;

import io.scalecube.configuration.RedisConfigurationService;
import io.scalecube.services.Microservices;

public class ConfigurationBootstrap {

  public static void main(String[] args) {
    
    RedisConfigurationService service = new RedisConfigurationService(Redisson.create());
    
    Microservices ms = Microservices.builder()
      .services(service)
      .build();
   
    System.out.println(ms.cluster().address());
  }

}

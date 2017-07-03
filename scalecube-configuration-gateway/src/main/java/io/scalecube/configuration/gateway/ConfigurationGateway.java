package io.scalecube.configuration.gateway;

import io.scalecube.configuration.api.ConfigurationService;
import io.scalecube.gateway.APIGateway;
import io.scalecube.services.Microservices;
import io.scalecube.transport.Address;

public class ConfigurationGateway {

  private static Microservices bootstrap() throws Exception {
    final Microservices node = Microservices.builder()
        .seeds(Address.create("10.0.75.1",4801))
        .build();
    return node;
  }

  public static void main(String[] args) throws Exception {
    final Microservices node = bootstrap();
    start(8080,node);
  }

  public static void start(int port, Microservices seed) {
    ConfigurationService service = seed.proxy().api(ConfigurationService.class).create();
    APIGateway.builder().port(8081)
        .instance(service).api(ConfigurationService.class)
        .route("POST", "/configuration/save").to("save")
        .route("POST", "/configuration/entries").to("entries")
        .route("POST", "/configuration/fetch").to("fetch")
        .route("POST", "/configuration/delete").to("delete");
    
  }
}

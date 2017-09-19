package io.scalecube.server;

import io.scalecube.services.Microservices;

public class ConfigurationGateway {

  public static void start(int port, Microservices seed) {
    Main.start(port, seed);
  }
}

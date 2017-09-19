package io.scalecube.server;

import io.scalecube.services.Microservices;

public class AccountGateway {

  public static void start(int port, Microservices seed) {
    Main.start(port, seed);
  }

}

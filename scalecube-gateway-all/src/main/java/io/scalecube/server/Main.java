package io.scalecube.server;

import io.scalecube.account.RedisAccountService;
import io.scalecube.configuration.RedisConfigurationService;
import io.scalecube.packages.utils.Logo;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.server.Main;
import io.scalecube.services.Microservices;

import org.redisson.api.RedissonClient;

import java.io.IOException;

public class Main {

  /**
   * app main.
   * 
   * @param args application arguments.
   * @throws IOException on error.
   */
  public static void main(String[] args) throws IOException {

    PackageInfo info = new PackageInfo();
    
    RedissonClient client = info.redisClient();

    Microservices seed = Microservices.builder().seeds(info.seedAddress())
        .services(RedisAccountService.builder().redisson(client).build(),
            RedisConfigurationService.builder().redisson(client).build())
        .build();

    AccountGateway.start(info.gatewayPort(), seed);
    ConfigurationGateway.start(info.gatewayPort(), seed);

    Logo.builder().tagVersion(info.version())
        .port(seed.cluster().address().port() + "")
        .header("Api-Gateway port: " + info.gatewayPort())
        .ip(seed.cluster().address().host())
        .group(info.groupId())
        .artifact(info.artifactId())
        .javaVersion(info.java())
        .osType(info.os())
        .pid(info.pid())
        .website().draw();
  }
}

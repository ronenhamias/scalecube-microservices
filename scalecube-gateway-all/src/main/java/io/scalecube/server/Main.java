package io.scalecube.server;

import io.scalecube.account.RedisAccountService;
import io.scalecube.configuration.RedisConfigurationService;
import io.scalecube.packages.utils.Logo;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.services.Microservices;

import org.redisson.api.RedissonClient;

public class Main {

  /**
   * app main.
   * 
   * @param args application arguments.
   */
  public static void main(String[] args) {

    PackageInfo info = new PackageInfo();

    RedissonClient client = info.redisClient();

    Microservices seed = Microservices.builder().seeds(info.seedAddress())
        .services(RedisAccountService.builder().redisson(client).build(),
            RedisConfigurationService.builder().redisson(client).build())
        .startAwait();

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

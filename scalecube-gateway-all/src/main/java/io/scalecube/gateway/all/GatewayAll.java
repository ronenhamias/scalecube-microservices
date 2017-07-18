package io.scalecube.gateway.all;

import io.scalecube.account.RedisAccountService;
import io.scalecube.account.db.RedisOrganizations;
import io.scalecube.account.gateway.AccountGateway;
import io.scalecube.configuration.RedisConfigurationService;
import io.scalecube.configuration.gateway.ConfigurationGateway;
import io.scalecube.gateway.ApiGateway;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.packages.utils.Logo;
import io.scalecube.services.Microservices;

import org.rapidoid.setup.On;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class GatewayAll {

  /**
   * app main.
   * 
   * @param args application arguments.
   * @throws IOException on error.
   */
  public static void main(String[] args) throws IOException {

    PackageInfo info = new PackageInfo();

    Config config = new Config();
    config.useSingleServer()
        .setAddress(info.redisAddress())
        .setConnectionPoolSize(10);

    RedissonClient client = Redisson.create(config);

    Microservices seed = Microservices.builder().seeds(info.seedAddress())
        .services(
            new RedisAccountService(client),
            new RedisConfigurationService(client))
        .build();

    AccountGateway.start(8081, seed);
    ConfigurationGateway.start(8081, seed);

    Logo.builder().tagVersion(info.version())
        .port(seed.cluster().address().port() + "")
        .ip(seed.cluster().address().host())
        .group(info.groupId())
        .artifact(info.artifactId())
        .javaVersion(info.java())
        .osType(info.os())
        .pid(info.pid())
        .website().draw();
  }
}

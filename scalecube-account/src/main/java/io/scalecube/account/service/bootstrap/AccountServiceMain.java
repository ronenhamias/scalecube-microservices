package io.scalecube.account.service.bootstrap;

import io.scalecube.account.RedisAccountService;
import io.scalecube.packages.utils.Logo;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.services.Microservices;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class AccountServiceMain {

  /**
   * AccountBootstrap main.
   * 
   * @param args appication params.
   */
  public static void main(String[] args) {

    PackageInfo info = new PackageInfo();

    RedissonClient client = redisClient(info);

    final Microservices seed;
    if (info.seedAddress() != null) {
      seed = Microservices.builder()
          .services(new RedisAccountService(client))
          .seeds(info.seedAddress()).build();
    } else {
      seed = Microservices.builder()
          .services(new RedisAccountService(client))
          .build();
    }

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

  private static RedissonClient redisClient(PackageInfo info) {
    Config config = new Config();
    config.useSingleServer()
        .setAddress(info.redisAddress())
        .setConnectionPoolSize(10);

    RedissonClient client = Redisson.create(config);
    return client;
  }
}

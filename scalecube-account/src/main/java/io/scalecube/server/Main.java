package io.scalecube.server;

import io.scalecube.account.RedisAccountService;
import io.scalecube.packages.utils.Logo;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.services.Microservices;

public class Main {

  /**
   * AccountBootstrap main.
   * 
   * @param args appication params.
   */
  public static void main(String[] args) {

    PackageInfo info = new PackageInfo();

    final Microservices seed;
    if (info.seedAddress() != null) {
      seed = Microservices.builder()
          .services(RedisAccountService.builder().redisson(info.redisClient()).build())
          .seeds(info.seedAddress()).startAwait();
    } else {
      seed = Microservices.builder()
          .services(RedisAccountService.builder().redisson(info.redisClient()).build())
          .startAwait();
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

}

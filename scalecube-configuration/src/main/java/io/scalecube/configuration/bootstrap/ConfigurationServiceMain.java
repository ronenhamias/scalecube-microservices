package io.scalecube.configuration.bootstrap;

import io.scalecube.configuration.RedisConfigurationService;
import io.scalecube.packages.utils.Logo;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.services.Microservices;

import org.redisson.Redisson;

public class ConfigurationServiceMain {

  /**
   * Main Redis configuration main.
   * 
   * @param args application params.
   */
  public static void main(String[] args) {

    PackageInfo info = new PackageInfo();
    
    RedisConfigurationService service = new RedisConfigurationService(Redisson.create());

    Microservices seed = Microservices.builder()
        .seeds(info.seedAddress())
        .services(service)
        .build();

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

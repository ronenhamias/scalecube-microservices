package io.scalecube.configuration.gateway.bootstrap;

import io.scalecube.configuration.api.ConfigurationService;
import io.scalecube.gateway.ApiGateway;
import io.scalecube.packages.utils.Logo;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.services.Microservices;

public class ConfigurationGatewayMain {

  public static void main(String[] args) throws Exception {
    PackageInfo info = new PackageInfo();

    final Microservices seed;

    if (info.seedAddress() != null) {
      seed = Microservices.builder().seeds(info.seedAddress()).build();
    } else {
      seed = Microservices.builder().build();
    }

    start(info.gatewayPort(), seed);

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

  /**
   * start the configuration gateway on specific port.
   * 
   * @param port to start serve requests from.
   * @param seed as access point to the cluster.
   */
  public static void start(int port, Microservices seed) {
    ConfigurationService service = seed.proxy().api(ConfigurationService.class).create();
    ApiGateway.builder().port(port)
        .instance(service).api(ConfigurationService.class)
        .crossOriginResourceSharing()
        .route("POST", "/configuration/save").to("save")
        .route("POST", "/configuration/entries").to("entries")
        .route("POST", "/configuration/fetch").to("fetch")
        .route("POST", "/configuration/delete").to("delete");
  }
}

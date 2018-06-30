package io.scalecube.server;

import io.scalecube.cluster.ClusterConfig;
import io.scalecube.cluster.ClusterConfig.Builder;
import io.scalecube.packages.utils.Cli;
import io.scalecube.packages.utils.Logo;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.services.Microservices;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Seed Node server.
 *
 */
public class Main {

  /**
   * main seed node.
   * 
   * @param args none.
   */
  public static void main(String[] args) throws InterruptedException {
    PackageInfo packageInfo = new PackageInfo();
    Microservices seed;

    HashMap<String, String> map = new HashMap<>();
    map.put("hostname", packageInfo.hostname());
    map.put("type", "seed");
    Builder conf = ClusterConfig.builder().metadata(map);

    if (packageInfo.seedAddress() == null) {
      Cli.prln("Seed Host and port not specified - running as standalone seed.");
      seed = Microservices.builder().clusterConfig(conf).startAwait();
    } else {
      Cli.prln("Seed Host and port specified - trying to join cluster: " + Arrays.toString(packageInfo.seedAddress()));
      seed = Microservices.builder().clusterConfig(conf)
          .seeds(packageInfo.seedAddress()).startAwait();
    }

    Logo.builder().tagVersion(packageInfo.version())
        .port(String.valueOf(seed.cluster().address().port()))
        .header("Service Port: " + seed.serviceAddress().port())
        .ip(seed.cluster().address().host())
        .group(packageInfo.groupId())
        .artifact(packageInfo.artifactId())
        .javaVersion(packageInfo.java())
        .osType(packageInfo.os())
        .pid(packageInfo.pid())
        .hostname(packageInfo.hostname())
        .website().draw();
    
    if (args.length > 0) {
      Cli cli = new Cli(seed);
      cli.start();
    }

    Thread.currentThread().join();
  }
}

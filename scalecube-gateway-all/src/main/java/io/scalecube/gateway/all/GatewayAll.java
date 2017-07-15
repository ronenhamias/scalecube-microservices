package io.scalecube.gateway.all;

import io.scalecube.account.RedisAccountService;
import io.scalecube.account.db.RedisOrganizations;
import io.scalecube.account.gateway.AccountGateway;
import io.scalecube.configuration.RedisConfigurationService;
import io.scalecube.configuration.gateway.ConfigurationGateway;
import io.scalecube.services.Microservices;

import org.rapidoid.setup.On;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Properties;

public class GatewayAll {

  /**
   * app main.
   * 
   * @param args application arguments.
   * @throws IOException on error.
   */
  public static void main(String[] args) throws IOException {

    Info info = new Info();

    Config config = new Config();
    config.useSingleServer()
        .setAddress(info.redisAddress())
        .setConnectionPoolSize(10);

    RedissonClient client = Redisson.create(config);

    Microservices seed = Microservices.builder().build();
    String address = seed.cluster().address().host();
    RedisOrganizations accountManager = new RedisOrganizations(client);
    accountManager.clearAll();

    RedisAccountService account = new RedisAccountService(client);
    RedisConfigurationService configuration = new RedisConfigurationService(client);

    Microservices.builder().port(4801)
        .services(account, configuration)
        .seeds(seed.cluster().address())
        .build();

    AccountGateway.start(8080, seed);
    ConfigurationGateway.start(8080, seed);
    if (args.length == 0) {
      On.port(8080).get("/").html(www(null));
    } else {
      On.port(8080).get("/").html(www(args[0]));
    }


    Logo.builder().tagVersion(info.version())
        .port("4801")
        .ip(address)
        .group(info.groupId())
        .artifact(info.artifactId())
        .javaVersion(info.java())
        .osType(info.os())
        .pid(info.pid())
        .website().draw();
  }



  private static String www(String workFir) {
    try {
      URL url = GatewayAll.class.getResource("index.html");
      File file;
      if (url != null) {
        file = new File(url.getPath());
      } else {
        file = new File(workFir + "/index.html");
      }
      System.out.println("file path:" + file.toPath());
      return new String(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


}

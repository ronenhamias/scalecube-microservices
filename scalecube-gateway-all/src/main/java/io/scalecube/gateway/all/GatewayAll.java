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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class GatewayAll {

  public static void main(String[] args) {
    RedissonClient client = Redisson.create();
    RedisOrganizations accountManager = new RedisOrganizations(client);
    accountManager.clearAll();
    
    Microservices seed = Microservices.builder().build();
    
    RedisAccountService account = new RedisAccountService(client);
    RedisConfigurationService configuration  = new RedisConfigurationService(client);
    
    Microservices ms = Microservices.builder()
      .services(account,configuration)
      .seeds(seed.cluster().address())
      .build();
    
    AccountGateway.start(8080,seed);
    ConfigurationGateway.start(8080,seed);
    On.port(8080).get("/").html(www());
  }

  private static String www(){
    try {
      URL url = GatewayAll.class.getResource("index.html");
      File file = new File(url.getPath());
      return new String(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}

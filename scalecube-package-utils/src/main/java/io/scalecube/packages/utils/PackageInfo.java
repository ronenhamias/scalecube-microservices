package io.scalecube.packages.utils;

import io.scalecube.transport.Address;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Information provider about the environment and the package of this instance.
 *
 */
public class PackageInfo {

  private final Properties properties = new Properties();

  /**
   * Runtime Environment information provider.
   */
  public PackageInfo() {
    if (System.getenv("SC_HOME") != null) {
      String path = System.getenv("SC_HOME");

      try {
        FileInputStream in = new FileInputStream(path + "package.properties");
        properties.load(in);
      } catch (IOException e) {
        System.out.println("cannot open file: " + path + "package.properties cause:" + e.getCause());
        defaultProps();
      }
    } else {
      InputStream stream = PackageInfo.class.getResourceAsStream("package.properties");
      if (stream != null) {
        try {
          properties.load(stream);
        } catch (IOException e) {
          defaultProps();
        }
      } else {
        defaultProps();
      }
    }
  }

  private void defaultProps() {
    properties.put("version", "Development");
    properties.put("groupId", "Development");
    properties.put("artifactId", "Development");
    properties.put("version", "Development");
    properties.put("version", "Development");
  }

  public String version() {
    return properties.getProperty("version");
  }

  public String groupId() {
    return properties.getProperty("groupId");
  }

  public String artifactId() {
    return properties.getProperty("artifactId");
  }

  public String java() {
    return System.getProperty("java.version");
  }

  public String os() {
    return System.getProperty("os.name");
  }

  public String pid() {
    String vmName = ManagementFactory.getRuntimeMXBean().getName();
    int pids = vmName.indexOf("@");
    String pid = vmName.substring(0, pids);
    return pid;
  }

  /**
   * resolves redis address.
   */
  public String redisAddress() {
    return getVariable("REDIS_ADDRESS", "redis://localhost:6379");
  }

  /**
   * Returns API Gateway API.
   */
  public int gatewayPort() {
    String port = getVariable("API_GATEWAY_PORT", "8081");
    return Integer.valueOf(port);
  }

  /**
   * Resolve seed address from environment variable or system property.
   * 
   * @return seed address as string for example localhost:4801.
   */
  public Address[] seedAddress() {
    String list = getVariable("SC_SEED_ADDRESS", null);
    if (list != null && !list.isEmpty()) {
      String[] hosts = list.split(",");
      List<Address> seedList = Arrays.asList(hosts).stream().filter(predicate -> !predicate.isEmpty())
          .map(mapper -> mapper.trim())
          .map(hostAndPort -> {
            return Address.from(hostAndPort);
          }).collect(Collectors.toList());
      return seedList.toArray(new Address[seedList.size()]);
    } else {
      return null;
    }
  }

  private String getVariable(String name, String defaultValue) {
    if (System.getenv(name) != null) {
      return System.getenv(name);
    }
    if (System.getProperty(name) != null) {
      return System.getProperty(name);
    }
    return defaultValue;
  }

  /**
   * Resolve redis client configuration and return a client.
   * @return RedissonClient to connect to redis server.
   */
  public RedissonClient redisClient() {
    Config config = new Config();
    config.useSingleServer()
        .setAddress(redisAddress())
        .setConnectionPoolSize(10);

    RedissonClient client = Redisson.create(config);
    return client;
  }



}

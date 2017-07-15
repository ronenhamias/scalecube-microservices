package io.scalecube.gateway.all;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.Properties;

public class Info {

  final Properties properties = new Properties();

  /**
   * Runtime Environment information provider.
   */
  public Info() {
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
      InputStream stream = Info.class.getResourceAsStream("package.properties");
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
    String address = getVariable("REDIS_HOST", "localhost");
    String port = getVariable("REDIS_PORT", "6379");

    System.out.println("ENV - REDIS HOST: " + address);
    System.out.println("ENV - REDIS PORT: " + port);

    return "redis://" + address + ":" + port;
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
}

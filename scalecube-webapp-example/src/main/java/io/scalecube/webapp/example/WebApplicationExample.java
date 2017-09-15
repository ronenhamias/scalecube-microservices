package io.scalecube.webapp.example;

import org.rapidoid.setup.On;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;



public class WebApplicationExample {

  public static void main(String[] args) {
    On.port(8080).get("/").html(www());
  }

  private static String www() {
    try {
      URL url = WebApplicationExample.class.getResource("index.html");
      File file = null;
      if (url != null) {
        file = new File(url.getPath());
        System.out.println("file path:" + file.toPath());
        return new String(Files.readAllBytes(file.toPath()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}

package io.scalecube.packages.utils;

import io.scalecube.services.Microservices;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Cli {

  private Microservices microservices;

  public Cli(Microservices seed) {
   this.microservices = seed;
  }

  public void start() {
    // create the command line parser
    CommandLineParser parser = new DefaultParser();
    
    // create the Options
    Options options = new Options();
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      try {
        String command = buffer.readLine();
        if (command != null) {
          CommandLine line = parser.parse(options, command.split(" "));

          if (!line.getArgList().isEmpty() && line.getArgs()[0].equals("cluster")) {
            ClusterCli.command(microservices, line);
          }
          
        } else {
          Cli.prln("cluster member is shutting down...");
          microservices.shutdown().get();
          Cli.prln("Good bye");
          System.exit(0);
        }
      } catch (Exception e) {

      }
    }
  }



  public static void pr(String line) {
    System.out.print(line);
  }

  public static String tab(int j) {
    StringBuilder builder = new StringBuilder("\t");
    for (int i = 0; i < j; i++) {
      builder.append("\t");
    }
    return builder.toString();
  }

  public static void prln(String line) {
    System.out.println(line);
  }
}

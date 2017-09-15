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

  /**
   * start cli.
   */
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
        System.out.println(e.getMessage());
      }
    }
  }


  /**
   * print string.
   * 
   * @param line to print out.
   */
  public static void pr(String line) {
    System.out.print(line);
  }

  /**
   * append tab for current index.
   * 
   * @param index to append tab from;
   * @return String appended with tab.
   */
  public static String tab(int index) {
    StringBuilder builder = new StringBuilder("\t");
    for (int i = 0; i < index; i++) {
      builder.append("\t");
    }
    return builder.toString();
  }

  public static void prln(String line) {
    System.out.println(line);
  }
}

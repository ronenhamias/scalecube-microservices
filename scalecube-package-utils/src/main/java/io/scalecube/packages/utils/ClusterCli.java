package io.scalecube.packages.utils;

import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.services.Microservices;

import org.apache.commons.cli.CommandLine;

public class ClusterCli {

  private static void print(Microservices seed) {
    Cli.prln("TYPE" + Cli.tab(1) + "MEMBER ID" + Cli.tab(2) + "IP ADDRESS" + Cli.tab(2) + "HOSTNAME");
    seed.cluster().members().forEach(member -> {
      Cli.pr(member.metadata().get("type") == null ? "member" : member.metadata().get("type"));
      Cli.pr(Cli.tab(1));
      Cli.pr(member.id());
      Cli.pr(Cli.tab(1));
      Cli.pr(member.address().toString());
      Cli.pr(Cli.tab(2));
      Cli.pr((member.metadata().get("hostname") == null ? " unknown" : member.metadata().get("hostname")));
      if (seed.cluster().member().id().equals(member.id())) {
        Cli.pr("  <-- this\n");
      }
      Cli.prln("");
    });
  }

  public static void listen(Microservices seed){
    seed.cluster().listenMembership().subscribe(onNext -> {
      if (onNext.type().equals(MembershipEvent.Type.ADDED)) {
        System.out
            .println("** New Member Added **\n- id:" + onNext.member().id() + " address:" + onNext.member().address());
      } else if (onNext.type().equals(MembershipEvent.Type.REMOVED)) {
        Cli.prln(
            "** Member was Removed **\n- id:" + onNext.oldMember().id() + " address:" + onNext.oldMember().address());
      } else if (onNext.type().equals(MembershipEvent.Type.UPDATED)) {
        Cli.prln("** Member Updated **\n- id:" + onNext.member().id() + " address:" + onNext.member().address());
      }
    });
  }
  public static void command(Microservices seed, CommandLine line) {
    if (line.getArgs().length >= 2 && line.getArgs()[1].equals("ls")) {
      print(seed);
    } else if (line.getArgs().length >= 2 && line.getArgs()[1].equals("listen")) {
        listen(seed);
    } else if ((line.getArgs().length==1) || (line.getArgs().length >= 2 && line.getArgs()[1].equals("help"))) {
      help();
    }
  }

  public static void help() {
    Cli.prln("Usage:");
    Cli.prln("  cluster [option]   example:   cluster ls");
    Cli.prln("");
    Cli.prln("options:");
    Cli.prln("  help \t\t list cluster commands and options");
    Cli.prln("  ls \t\t list cluster members");
    Cli.prln("  listen \t\t listen on cluster membership events");
  }

}

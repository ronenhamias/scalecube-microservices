package io.scalecube.seed;

import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.packages.utils.Logo;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.services.Microservices;

/**
 * Seed Node server.
 *
 */
public class Server {

  /**
   * main seed node.
   * 
   * @param args none.
   */
  public static void main(String[] args) {
    PackageInfo packageInfo = new PackageInfo();
    Microservices seed;

    if (packageInfo.seedAddress() == null) {
      System.out.println("Seed Host and port not specified - running as standalone seed.");
      seed = Microservices.builder().build();
    } else {
      System.out.println("Seed Host and port specified - trying to join cluster: " + packageInfo.seedAddress());
      seed = Microservices.builder().seeds(packageInfo.seedAddress()).build();
    }

    Logo.builder().tagVersion(packageInfo.version())
        .port(String.valueOf(seed.cluster().address().port()))
        .header("Service Port: " + seed.sender().address().port())
        .ip(seed.cluster().address().host())
        .group(packageInfo.groupId())
        .artifact(packageInfo.artifactId())
        .javaVersion(packageInfo.java())
        .osType(packageInfo.os())
        .pid(packageInfo.pid())
        .website().draw();

    seed.cluster().listenMembership().subscribe(onNext -> {
      if (onNext.type().equals(MembershipEvent.Type.ADDED)) {
        System.out
            .println("** New Member Added **\n- id:" + onNext.member().id() + " address:" + onNext.member().address());
      } else if (onNext.type().equals(MembershipEvent.Type.REMOVED)) {
        System.out.println(
            "** Member was Removed **\n- id:" + onNext.oldMember().id() + " address:" + onNext.oldMember().address());
      } else if (onNext.type().equals(MembershipEvent.Type.UPDATED)) {
        System.out
            .println("** Member Updated **\n- id:" + onNext.member().id() + " address:" + onNext.member().address());
      }
      System.out.println("");
      print(seed);
    });
    print(seed);
  }

  private static void print(Microservices seed) {
    System.out.println("=========================================");
    System.out.println("- Member id\t\t| Member Address");
    System.out.println("=========================================");
    seed.cluster().members().forEach(member -> {
      System.out.print(member.id() + "\t| ");
      System.out.print(member.address() + "\n");
    });

    System.out.println("");
  }
}

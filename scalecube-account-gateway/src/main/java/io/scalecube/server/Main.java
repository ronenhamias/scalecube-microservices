package io.scalecube.server;

import io.scalecube.account.api.AccountService;
import io.scalecube.gateway.ApiGateway;
import io.scalecube.packages.utils.Logo;
import io.scalecube.packages.utils.PackageInfo;
import io.scalecube.services.Microservices;

public class Main {

  /**
   * main bootstrap starting account gateway instance.
   * @param args none.
   * @throws Exception in case of error.
   */
  public static void main(String[] args) throws Exception {
    PackageInfo info = new PackageInfo();

    final Microservices seed;

    if (info.seedAddress() != null) {
      seed = Microservices.builder().seeds(info.seedAddress()).build();
    } else {
      seed = Microservices.builder().build();
    }
    
    start(info.gatewayPort(), seed);

    Logo.builder().tagVersion(info.version())
        .port(seed.cluster().address().port() + "")
        .header("Api-Gateway port: " + info.gatewayPort())
        .ip(seed.cluster().address().host())
        .group(info.groupId())
        .artifact(info.artifactId())
        .javaVersion(info.java())
        .osType(info.os())
        .pid(info.pid())
        .website().draw();
  }

  /**
   * Start the account gateway on a specific port.
   * 
   * @param port to start the gateway on.
   * @param seed node to join the cluster with.
   */
  public static void start(int port, Microservices seed) {
    AccountService accountService = seed.proxy().api(AccountService.class).create();

    ApiGateway.builder().port(port)
        .instance(accountService).api(AccountService.class)
        .crossOriginResourceSharing()
        .route("POST", "/account/users/token/login").to("register")
        .route("POST", "/account/users/search").to("searchUser")
        .route("POST", "/account/organization/create").to("createOrganization")
        .route("POST", "/account/organization/api-key/add").to("addOrganizationApiKey")
        .route("POST", "/account/organization/api-key/delete").to("deleteOrganizationApiKey")
        .route("POST", "/account/organization/update").to("updateOrganization")
        .route("POST", "/account/organization/delete").to("deleteOrganization")
        .route("POST", "/account/organization/get").to("getOrganization")
        .route("POST", "/account/organization/membership").to("getUserOrganizationsMembership")
        .route("POST", "/account/organization/members/get").to("getOrganizationMembers")
        .route("POST", "/account/organization/members/kickout").to("kickoutMember")
        .route("POST", "/account/organization/members/invite").to("inviteMember")
        .route("POST", "/account/organization/members/leave").to("leaveOrganization");

  }
}

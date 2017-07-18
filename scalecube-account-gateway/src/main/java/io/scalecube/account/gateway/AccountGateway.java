package io.scalecube.account.gateway;

import io.scalecube.account.api.AccountService;
import io.scalecube.gateway.ApiGateway;
import io.scalecube.services.Microservices;
import io.scalecube.transport.Address;

public class AccountGateway {

  private static Microservices connectSeed(String host, int port) throws Exception {

    final Microservices node = Microservices.builder()
        .seeds(Address.create("10.0.75.1", 4801))
        .build();
    return node;
  }

  public static void main(String[] args) throws Exception {
    final Microservices node = connectSeed("10.0.75.1", 4801);

    start(8080, node);
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

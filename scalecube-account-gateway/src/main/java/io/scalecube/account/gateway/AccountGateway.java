package io.scalecube.account.gateway;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import org.rapidoid.setup.On;

import io.scalecube.account.api.AccountService;
import io.scalecube.gateway.APIGateway;
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

  public static void start(int port, Microservices seed) {
    AccountService accountService = seed.proxy().api(AccountService.class).create();

    APIGateway.builder().port(port)
        .instance(accountService).api(AccountService.class)
        .route("POST", "/account/users/register").to("register")
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

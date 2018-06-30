package io.scalecube.account;

import static org.junit.Assert.assertNotNull;

import io.scalecube.account.api.AccountService;
import io.scalecube.account.api.CreateOrganizationRequest;
import io.scalecube.account.api.CreateOrganizationResponse;
import io.scalecube.account.api.DeleteOrganizationRequest;
import io.scalecube.account.api.DeleteOrganizationResponse;
import io.scalecube.account.api.GetMembershipRequest;
import io.scalecube.account.api.GetMembershipResponse;
import io.scalecube.account.api.GetOrganizationRequest;
import io.scalecube.account.api.GetOrganizationResponse;
import io.scalecube.account.api.OrganizationService;
import io.scalecube.account.api.Token;
import io.scalecube.account.api.User;
import io.scalecube.test.utils.Await;
import io.scalecube.test.utils.Await.AwaitLatch;
import io.scalecube.testlib.BaseTest;

import org.junit.Test;
import org.redisson.Redisson;

import java.util.concurrent.TimeUnit;

public class RedisAccountServiceTest extends BaseTest {
  Token token = new Token("google", "user1");

  final AccountService account;

  final OrganizationService organization;

  public RedisAccountServiceTest() throws Exception {
    RedisAccountService redis = RedisAccountService.builder()
        .redisson(Redisson.create())
        .mock(new User("1", "user1@gmail.com", true, "name 1", "http://picture.jpg", "EN", "fname", "lname", null))
        .build();
    organization = redis;
    account = redis;
  }

  @Test
  public void testRegisterUser() throws Exception {
    AwaitLatch<User> await = Await.one();
    account.register(token).subscribe(await::result, await::error);
    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }

  @Test
  public void testCreateOrganization() throws Exception {
    AwaitLatch<CreateOrganizationResponse> await = Await.one();

    account.register(token)
        .subscribe(user -> organization.createOrganization(new CreateOrganizationRequest("myTestOrg5", token, "email"))
            .subscribe(await::result, await::error));
    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }

  @Test
  public void testGetOrganizationDetails() throws Exception {
    AwaitLatch<GetMembershipResponse> await = Await.one();

    account.register(token)
        .subscribe(user -> organization.getUserOrganizationsMembership(new GetMembershipRequest(token))
            .subscribe(await::result, await::error));
    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }

  @Test
  public void testGetOrganization() throws Exception {
    AwaitLatch<GetOrganizationResponse> await = Await.one();

    account.register(token)
        .subscribe(user -> organization.createOrganization(new CreateOrganizationRequest("org1", token, "email"))
            .subscribe(createOrganizationResponse -> organization
                .getOrganization(new GetOrganizationRequest(token, createOrganizationResponse.id()))
                .subscribe(await::result, await::error)));

    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }

  @Test
  public void testDeleteOrganization() throws Exception {
    AwaitLatch<DeleteOrganizationResponse> await = Await.one();

    account.register(token)
        .subscribe(user -> organization.createOrganization(new CreateOrganizationRequest("org2", token, "email"))
            .subscribe(createOrganizationResponse -> organization
                .deleteOrganization(new DeleteOrganizationRequest(token, createOrganizationResponse.id()))
                .subscribe(await::result, await::error)));

    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }
}

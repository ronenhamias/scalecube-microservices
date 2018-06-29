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

  public RedisAccountServiceTest() throws Exception {
    account = RedisAccountService.builder()
        .redisson(Redisson.create())
        .mock(new User("1", "user1@gmail.com", true, "name 1", "http://picture.jpg", "EN", "fname", "lname", null))
        .build();
  }

  @Test
  public void testRegisterUser() throws Exception {
    AwaitLatch<User> await = Await.one();
    account.register(token).doAfterSuccessOrError((success, error) -> {
      if (error == null) {
        await.result(success);
      } else {
        await.error(error);
      }
    }).subscribe();
    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }

  @Test
  public void testCreateOrganization() throws Exception {
    AwaitLatch<CreateOrganizationResponse> await = Await.one();

    account.register(token).doAfterTerminate(() -> {
      account.createOrganization(new CreateOrganizationRequest("myTestOrg5", token, "email"))
          .doAfterSuccessOrError((success, error) -> {
            if (error == null) {
              await.result(success);
            } else {
              await.error(error);
            }

          }).subscribe();
    }).subscribe();
    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }

  @Test
  public void testGetOrganizationDetails() throws Exception {
    AwaitLatch<GetMembershipResponse> await = Await.one();

    account.register(token).doAfterTerminate(() -> {
      account.getUserOrganizationsMembership(new GetMembershipRequest(token))
          .doAfterSuccessOrError((success, error) -> {
            if (error == null) {
              await.result(success);
            } else {
              await.error(error);
            }
          }).subscribe();
    }).subscribe();
    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }

  @Test
  public void testGetOrganization() throws Exception {
    AwaitLatch<GetOrganizationResponse> await = Await.one();

    account.register(token).doAfterTerminate(() -> {
      account.createOrganization(new CreateOrganizationRequest("org1", token, "email"))
          .doAfterSuccessOrError((ack, nack) -> {
            account.getOrganization(new GetOrganizationRequest(token, ack.id()))
                .doAfterSuccessOrError((success, error) -> {
                  if (error == null) {
                    await.result(success);
                  } else {
                    await.error(error);
                  }
                }).subscribe();
          }).subscribe();
    }).subscribe();

    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }

  @Test
  public void testDeleteOrganization() throws Exception {
    AwaitLatch<DeleteOrganizationResponse> await = Await.one();

    account.register(token).doAfterTerminate(() -> {
      account.createOrganization(new CreateOrganizationRequest("org2", token, "email"))
          .doAfterSuccessOrError((ack, nack) -> {
            account.deleteOrganization(new DeleteOrganizationRequest(token, ack.id()))
                .doAfterSuccessOrError((success, error) -> {
                  if (error == null) {
                    await.result(success);
                  } else {
                    await.error(error);
                  }
                }).subscribe();
          }).subscribe();
    }).subscribe();

    await.timeout(2, TimeUnit.SECONDS);
    assertNotNull(await.result());
  }
}
package io.scalecube.account;

import static org.junit.Assert.assertNotNull;

import io.scalecube.account.api.AccountService;
import io.scalecube.account.api.CreateOrganizationRequest;
import io.scalecube.account.api.CreateOrganizationResponse;
import io.scalecube.account.api.GetMembershipRequest;
import io.scalecube.account.api.GetMembershipResponse;
import io.scalecube.account.api.Token;
import io.scalecube.account.api.User;
import io.scalecube.testlib.BaseTest;

import org.junit.Test;
import org.redisson.Redisson;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


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
    AtomicReference<User> responseRef = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(1);

    account.register(token).whenComplete((success, error) -> {
      if (error == null) {
        responseRef.set(success);
      }
      latch.countDown();
    });

    wait(latch, 5);

    assertNotNull(responseRef.get());
  }

  @Test
  public void testCreateOrganization() throws Exception {
    AtomicReference<CreateOrganizationResponse> responseRef = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(1);

    account.register(token).thenRun(() -> {
      account.createOrganization(new CreateOrganizationRequest("myTestOrg5", token, "email"))
          .whenComplete((success, error) -> {
            if (error == null) {
              responseRef.set(success);
            }
            latch.countDown();
          });
    });
    wait(latch, 5);

    assertNotNull(responseRef.get());
  }


  @Test
  public void testGetOrganizationDetails() throws Exception {
    AtomicReference<GetMembershipResponse> responseRef = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(1);

    account.register(token).thenRun(() -> {
      account.getUserOrganizationsMembership(new GetMembershipRequest(token))
          .whenComplete((success, error) -> {
            if (error == null) {
              responseRef.set(success);
            }
          });
    });
    wait(latch, 5);

    assertNotNull(responseRef.get());
  }

  private void wait(CountDownLatch latch, int timeout) {
    try {
      latch.await(timeout, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      System.out.println(ex);
    }
  }
}

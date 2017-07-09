package io.scalecube.account;

import io.scalecube.account.api.AccountService;
import io.scalecube.account.api.CreateOrganizationRequest;
import io.scalecube.account.api.GetMembershipRequest;
import io.scalecube.account.api.Token;

import org.redisson.Redisson;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;


public class TestRedisAccountService extends TestCase {
  String googleToken = "";

  final AccountService account;

  public TestRedisAccountService() throws Exception {
    account = new RedisAccountService(Redisson.create());
  }

  public void testCreateOrganization() throws Exception {

    CountDownLatch latch = new CountDownLatch(1);
    Token token = new Token("google", googleToken);
    account.createOrganization(new CreateOrganizationRequest("myTestOrg5", token, "email"))
        .whenComplete((success, error) -> {
          if (error == null) {
            System.out.println(success);
            latch.countDown();
          }
        });
    
    wait(latch,5);
    
  }

  

  public void testGetOrganizationDetails() throws Exception {
    Token token = new Token("google", googleToken);
    account.getUserOrganizationsMembership(new GetMembershipRequest(token))
        .whenComplete((success, error) -> {
          if (error == null) {
            System.out.println(success);
          }
        });
  }

  private void wait(CountDownLatch latch, int timeout) {
    try {
      latch.await(timeout, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      System.out.println(ex);
    }
  }
}

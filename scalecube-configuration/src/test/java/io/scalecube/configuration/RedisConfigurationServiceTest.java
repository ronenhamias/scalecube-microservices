package io.scalecube.configuration;

import static org.junit.Assert.assertEquals;

import io.scalecube.account.api.Token;
import io.scalecube.account.api.User;
import io.scalecube.configuration.api.Acknowledgment;
import io.scalecube.configuration.api.DeleteRequest;
import io.scalecube.configuration.api.FetchRequest;
import io.scalecube.configuration.api.FetchResponse;
import io.scalecube.configuration.api.SaveRequest;
import io.scalecube.configuration.db.redis.MockAccountService;
import io.scalecube.test.utils.Await;
import io.scalecube.test.utils.Await.AwaitLatch;
import io.scalecube.testlib.BaseTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.redisson.Redisson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisConfigurationServiceTest extends BaseTest {

  private final ObjectMapper mapper = new ObjectMapper();
  private final RedisConfigurationService service;
  private final Map<String, String> claims = new HashMap<>();
  private final Token token = new Token("test", "1234");

  public RedisConfigurationServiceTest() {
    claims.put("permissions-level", "write");
    User mockUser1 =
        new User("1", "user1@gmail.com", true, "name 1", "http://picture.jpg", "EN", "fname", "lname", claims);

    service = RedisConfigurationService.builder()
        .redisson(Redisson.create())
        .mock(new MockAccountService(mockUser1))
        .build();
  }

  @Test
  public void testSaveFetch() {
    AwaitLatch<String> latch = Await.one();

    JsonNode json = mapper.valueToTree("1");

    service.save(new SaveRequest(token, "collection1", "key1", json))
        .flatMap(success -> service.fetch(new FetchRequest(token, "collection1", "key1")))
        .subscribe(response -> latch.result(response.value().toString()), latch::error);

    latch.timeout(5, TimeUnit.SECONDS);
    assertEquals("\"1\"", latch.result());
  }

  @Test
  public void testSaveDelete() {
    AwaitLatch<Acknowledgment> latch = Await.one();

    JsonNode json = mapper.valueToTree("1");

    service.save(new SaveRequest(token, "collection1", "key1", json))
        .flatMap(success -> service.delete(new DeleteRequest(token, "collection1", "key1")))
        .subscribe(latch::result, latch::error);

    latch.timeout(5, TimeUnit.SECONDS);
    assertEquals(Acknowledgment.class, latch.result().getClass());
  }

  @Test
  public void testSaveEntries() {
    AwaitLatch<FetchResponse[]> latch = Await.one();

    JsonNode json = mapper.valueToTree("1");

    service.save(new SaveRequest(token, "collection1", "key1", json))
        .flatMap(success -> service.entries(new FetchRequest(token, "collection1", "key1")))
        .subscribe(response -> latch.result(response.entries()), latch::error);

    latch.timeout(5, TimeUnit.SECONDS);
    assertEquals(1, latch.result().length);
  }
}

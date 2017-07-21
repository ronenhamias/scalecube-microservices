package io.scalecube.configuration.db.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.scalecube.testlib.BaseTest;

import org.junit.Test;
import org.redisson.Redisson;

import java.util.Collection;

public class RedisStoreTest extends BaseTest {

  private final RedisStore<String> store = new RedisStore<>(Redisson.create());

  @Test
  public void test_put_get() {
    store.put("test_put_get", "key1", "1");
    String value = store.get("test_put_get", "key1");
    assertEquals("1", value);
  }

  @Test
  public void test_entries() {
    store.put("test_entries", "key1", "1");
    Collection<String> value = store.entries("test_entries");
    assertTrue(value.size() > 0);
  }

  @Test
  public void test_remove() {
    store.put("test_remove", "key1", "1");
    String value = store.remove("test_remove", "key1");
    Collection<String> entries = store.entries("test_remove");
    assertTrue(entries.size() == 0);
    assertEquals("1", value);
  }
}

package io.scalecube.configuration.db.redis;

import org.redisson.api.RedissonClient;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class RedisStore<T> {

  private RedissonClient redisson;

  public RedisStore(RedissonClient client) {
    redisson = client;
  }

  public T putIfAbsent(String collection, String key, T doc) {
    final ConcurrentMap<String, T> configurations = redisson.getMap(collection);
    return (T) configurations.putIfAbsent(key, doc);
  }

  public T put(String collection, String key, T doc) {
    final ConcurrentMap<String, T> configurations = redisson.getMap(collection);
    return (T) configurations.put(key, doc);
  }

  public T get(String collection, String key) {
    final ConcurrentMap<?, ?> configurations = redisson.getMap(collection);
    return (T) configurations.get(key);
  }

  public Collection<T> entries(String collection) {
    final ConcurrentMap<?, T> configurations = redisson.getMap(collection);
    Collection<T> set = configurations.values();
    return set;
  }

  public T remove(String collection, String key) {
    final ConcurrentMap<?, ?> configurations = redisson.getMap(collection);
    return (T) configurations.remove(key);
  }
}

package io.scalecube.configuration.db;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

public class RedisStore<T> {
  
  private RedissonClient redisson;
  
  public RedisStore() { 
    // connects to 127.0.0.1:6379 by default
    redisson = Redisson.create();
  }
  
  public T putIfAbsent(String collection,String key, T doc) {
    final ConcurrentMap configurations= redisson.getMap(collection);
    return (T) configurations.putIfAbsent(key,doc);
  }
  
  public T put(String collection,String key, T doc) {
    final ConcurrentMap configurations= redisson.getMap(collection);
    return (T) configurations.put(key,doc);
  }

  public T get(String collection, String key) {
    final ConcurrentMap configurations= redisson.getMap(collection);
    return (T) configurations.get(key);
  }

  public Collection<T> entries(String collection) {
    final ConcurrentMap configurations= redisson.getMap(collection);
    Collection set = configurations.values();
    return set;
  }
  
  public T remove(String collection, String key) {
    final ConcurrentMap configurations= redisson.getMap(collection);
    return (T) configurations.remove(key);
  }
}

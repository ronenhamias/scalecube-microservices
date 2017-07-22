package io.scalecube.test.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Await<T> {

  public static class AwaitLatch {
    private CountDownLatch latch;
    private Object result;
    private Throwable error;

    private AwaitLatch(int counts) {
      this.latch = new CountDownLatch(counts);
    }

    public void timeout(int timeout, TimeUnit timeUnit) {
      try {
        this.latch.await(timeout, timeUnit);
      } catch (Exception ex) {
      }
    }

    public void countDown() {
      latch.countDown();
    }

    public void result(Object response) {
      this.result = response;
      this.countDown();
    }

    public Object result() {
      return this.result;
    }

    public Object error() {
      return this.error;
    }

    public void error(Throwable error) {
      this.error = error;
      this.countDown();
    }
  }

  public static AwaitLatch one() {
    return new AwaitLatch(1);
  }
}

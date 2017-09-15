package io.scalecube.test.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Await {

  public static class AwaitLatch<T> {
    private CountDownLatch latch;
    private T result;
    private Throwable error;

    private AwaitLatch(int counts) {
      this.latch = new CountDownLatch(counts);
    }

    /**
     * return true if timeout has reached.
     * 
     * @param timeout amount of time to wait.
     * @param timeUnit time unit to wait.
     * @return true if timeout has reached/
     */
    public boolean timeout(int timeout, TimeUnit timeUnit) {
      try {
        this.latch.await(timeout, timeUnit);
        return false;
      } catch (Exception ex) {
        return true;
      }
    }

    public void countDown() {
      latch.countDown();
    }

    public void result(T response) {
      this.result = response;
      this.countDown();
    }

    public T result() {
      return (T) this.result;
    }

    public Object error() {
      return this.error;
    }

    public void error(Throwable error) {
      this.error = error;
      this.countDown();
    }
  }

  public static <T> AwaitLatch<T> one() {
    return new AwaitLatch<>(1);
  }
}

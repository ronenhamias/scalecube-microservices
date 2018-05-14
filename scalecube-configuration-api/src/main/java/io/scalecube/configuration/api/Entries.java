package io.scalecube.configuration.api;

public class Entries<T> {

  private T[] entries;

  /**
   * @deprecated only for serialization/deserialization
   */
  Entries() {}

  public Entries(T[] array) {
    this.entries = array;
  }

  public T[] entries() {
    return this.entries;
  }
}

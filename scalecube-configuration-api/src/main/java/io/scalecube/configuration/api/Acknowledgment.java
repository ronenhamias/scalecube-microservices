package io.scalecube.configuration.api;

public class Acknowledgment {

  private boolean ack;

  public Acknowledgment(boolean ack) {
    this.ack = ack;
  }
  
  @Override
  public String toString() {
    return "Acknowledgment [ack=" + ack + "]";
  }
}

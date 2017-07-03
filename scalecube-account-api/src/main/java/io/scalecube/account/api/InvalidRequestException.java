package io.scalecube.account.api;

public class InvalidRequestException extends Throwable {

  private static final long serialVersionUID = 1L;

  public InvalidRequestException(String message) {
    super(message);
  }

}

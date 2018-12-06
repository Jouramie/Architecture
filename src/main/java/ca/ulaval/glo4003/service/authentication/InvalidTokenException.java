package ca.ulaval.glo4003.service.authentication;

public class InvalidTokenException extends Exception {

  public InvalidTokenException() {
  }

  public InvalidTokenException(Exception cause) {
    super(cause);
  }
}

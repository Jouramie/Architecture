package ca.ulaval.glo4003.service.authentication;

public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException() {
  }

  public InvalidTokenException(Exception cause) {
    super(cause);
  }
}

package ca.ulaval.glo4003.service.authentication;

public class InvalidUserEmailException extends RuntimeException {

  public InvalidUserEmailException(Exception cause) {
    super(cause);
  }
}

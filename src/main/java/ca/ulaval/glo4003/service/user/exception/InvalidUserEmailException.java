package ca.ulaval.glo4003.service.user.exception;

public class InvalidUserEmailException extends RuntimeException {

  public InvalidUserEmailException(Exception cause) {
    super(cause);
  }
}

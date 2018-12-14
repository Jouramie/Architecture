package ca.ulaval.glo4003.service.user.exception;

public class UserDoesNotExistException extends RuntimeException {

  public UserDoesNotExistException(Exception cause) {
    super(cause);
  }
}

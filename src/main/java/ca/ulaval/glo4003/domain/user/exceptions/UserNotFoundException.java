package ca.ulaval.glo4003.domain.user.exceptions;

public class UserNotFoundException extends Exception {

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(Exception e) {
    super(e);
  }
}

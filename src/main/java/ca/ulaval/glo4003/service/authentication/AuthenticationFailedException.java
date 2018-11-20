package ca.ulaval.glo4003.service.authentication;

public class AuthenticationFailedException extends RuntimeException {

  public AuthenticationFailedException() {
    super();
  }

  public AuthenticationFailedException(Exception exception) {
    super(exception);
  }
}

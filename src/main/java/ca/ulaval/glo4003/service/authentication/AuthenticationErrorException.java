package ca.ulaval.glo4003.service.authentication;

public class AuthenticationErrorException extends RuntimeException {

  public AuthenticationErrorException() {
    super();
  }

  public AuthenticationErrorException(Exception exception) {
    super(exception);
  }
}

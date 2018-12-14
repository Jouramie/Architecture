package ca.ulaval.glo4003.domain.user.exception;

public class WrongRoleException extends Exception {
  public WrongRoleException(Exception e) {
    super(e);
  }
}

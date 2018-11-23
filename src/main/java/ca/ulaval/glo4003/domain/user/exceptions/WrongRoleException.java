package ca.ulaval.glo4003.domain.user.exceptions;

public class WrongRoleException extends Exception {
  public WrongRoleException(Exception e) {
    super(e);
  }
}

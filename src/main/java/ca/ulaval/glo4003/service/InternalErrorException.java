package ca.ulaval.glo4003.service;

public class InternalErrorException extends RuntimeException {
  public InternalErrorException(String msg) {
    super(msg);
  }
}

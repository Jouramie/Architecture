package ca.ulaval.glo4003.service.cart.exception;

public class StockNotInCartException extends RuntimeException {

  public StockNotInCartException(Exception cause) {
    super(cause);
  }
}

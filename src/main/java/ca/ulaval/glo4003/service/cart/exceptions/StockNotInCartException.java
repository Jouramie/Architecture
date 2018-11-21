package ca.ulaval.glo4003.service.cart.exceptions;

public class StockNotInCartException extends RuntimeException {

  public StockNotInCartException(Exception cause) {
    super(cause);
  }
}

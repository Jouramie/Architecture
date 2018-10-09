package ca.ulaval.glo4003.service.cart;

public class StockNotInCartException extends RuntimeException {

  public StockNotInCartException(String title) {
    super("Stock " + title + " is not in cart.");
  }
}

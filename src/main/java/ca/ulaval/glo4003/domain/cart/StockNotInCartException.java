package ca.ulaval.glo4003.domain.cart;

public class StockNotInCartException extends RuntimeException {
  private static final long serialVersionUID = 7073818388541397393L;

  public StockNotInCartException(String title) {
    super("Stock " + title + " is not in cart.");
  }
}

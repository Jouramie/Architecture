package ca.ulaval.glo4003.service.cart;

public class InvalidStockTitleException extends RuntimeException {
  public InvalidStockTitleException(String title) {
    super("Stock " + title + " does not exists.");
  }

  public InvalidStockTitleException(Exception cause) {
    super(cause);
  }
}

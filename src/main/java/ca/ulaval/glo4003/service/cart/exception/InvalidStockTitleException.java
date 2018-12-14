package ca.ulaval.glo4003.service.cart.exception;

public class InvalidStockTitleException extends RuntimeException {
  public InvalidStockTitleException(String title) {
    super("Stock " + title + " does not exist.");
  }

  public InvalidStockTitleException(Exception cause) {
    super(cause);
  }
}

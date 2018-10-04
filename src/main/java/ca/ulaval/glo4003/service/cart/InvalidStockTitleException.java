package ca.ulaval.glo4003.service.cart;

public class InvalidStockTitleException extends RuntimeException {
  private final Exception code;

  public InvalidStockTitleException(String title, Exception e) {
    super("Stock " + title + " does not exist.");
    code = e;
  }
}

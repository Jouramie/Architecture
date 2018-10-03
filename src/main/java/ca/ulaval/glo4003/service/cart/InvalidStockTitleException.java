package ca.ulaval.glo4003.service.cart;

public class InvalidStockTitleException extends RuntimeException {
  private static final long serialVersionUID = 4361406751157942405L;

  public InvalidStockTitleException(String title) {
    super("Stock " + title + " does not exist.");
  }
}

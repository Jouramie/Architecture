package ca.ulaval.glo4003.domain.stock;

public class StockNotFoundException extends RuntimeException {
  private static final long serialVersionUID = -3910986413507182983L;

  public StockNotFoundException(String message) {
    super(message);
  }
}

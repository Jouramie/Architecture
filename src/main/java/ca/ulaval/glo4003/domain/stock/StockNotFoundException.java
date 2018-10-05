package ca.ulaval.glo4003.domain.stock;

public class StockNotFoundException extends RuntimeException {

  public StockNotFoundException(String message) {
    super(message);
  }
}

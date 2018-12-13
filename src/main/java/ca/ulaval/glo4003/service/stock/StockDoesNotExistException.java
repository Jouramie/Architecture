package ca.ulaval.glo4003.service.stock;

public class StockDoesNotExistException extends RuntimeException {

  public StockDoesNotExistException(Exception cause) {
    super(cause);
  }

  public StockDoesNotExistException() {

  }
}

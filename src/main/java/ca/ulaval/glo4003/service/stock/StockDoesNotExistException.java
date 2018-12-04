package ca.ulaval.glo4003.service.stock;

public class StockDoesNotExistException extends RuntimeException {

  public StockDoesNotExistException(String title) {
    super(title);
  }
}

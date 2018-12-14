package ca.ulaval.glo4003.domain.stock.exception;

public class StockNotFoundException extends Exception {

  public final String title;

  public StockNotFoundException(String title) {
    this.title = title;
  }
}

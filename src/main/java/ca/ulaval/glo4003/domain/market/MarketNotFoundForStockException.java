package ca.ulaval.glo4003.domain.market;

public class MarketNotFoundForStockException extends Exception {

  public final String title;

  public MarketNotFoundForStockException(String stockTitle) {
    title = stockTitle;
  }
}

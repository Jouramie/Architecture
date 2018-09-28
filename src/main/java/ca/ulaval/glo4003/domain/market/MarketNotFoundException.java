package ca.ulaval.glo4003.domain.market;

public class MarketNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 6200697982696237431L;

  public MarketNotFoundException(String message) {
    super(message);
  }
}

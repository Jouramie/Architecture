package ca.ulaval.glo4003.domain.market.exception;

public class HaltedMarketException extends Exception {
  public final String message;

  public HaltedMarketException(String message) {
    this.message = message;
  }
}

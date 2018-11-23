package ca.ulaval.glo4003.service.cart;

public class HaltedMarketOnCheckoutException extends RuntimeException {
  public final String message;

  public HaltedMarketOnCheckoutException(String message) {
    this.message = message;
  }
}

package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.ws.api.cart.CartStockRequest;

public class CartStockRequestBuilder {
  public static final int DEFAULT_QUANTITY = 1;

  private int quantity = DEFAULT_QUANTITY;

  public CartStockRequestBuilder withQuantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public CartStockRequest build() {
    return new CartStockRequest(quantity);
  }
}

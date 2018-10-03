package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.ws.api.cart.CartStockRequest;

public class CartStockRequestBuilder {
  public static final int DEFAULT_QUANTITY = 1;

  public CartStockRequest build() {
    return new CartStockRequest(DEFAULT_QUANTITY);
  }

  public CartStockRequest build(Integer quantity) {
    return new CartStockRequest(quantity);
  }
}

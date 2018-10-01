package util;

import ca.ulaval.glo4003.ws.api.cart.CartStockRequest;

public class CartStockRequestBuilder {
  public static final String DEFAULT_TITLE = "RBS.l";
  public static final int DEFAULT_QUANTITY = 1;

  public CartStockRequest build() {
    return new CartStockRequest(DEFAULT_TITLE, DEFAULT_QUANTITY);
  }

  public CartStockRequest build(Integer quantity) {
    return new CartStockRequest(DEFAULT_TITLE, quantity);
  }
}

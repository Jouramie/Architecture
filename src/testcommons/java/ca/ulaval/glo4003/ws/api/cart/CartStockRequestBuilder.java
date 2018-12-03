package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.ws.api.cart.dto.CartStockRequestDto;

public class CartStockRequestBuilder {
  public static final int DEFAULT_QUANTITY = 1;

  private int quantity = DEFAULT_QUANTITY;

  public CartStockRequestBuilder withQuantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public CartStockRequestDto build() {
    return new CartStockRequestDto(quantity);
  }
}

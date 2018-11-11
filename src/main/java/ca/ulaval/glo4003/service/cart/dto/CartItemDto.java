package ca.ulaval.glo4003.service.cart.dto;

import java.math.BigDecimal;

public class CartItemDto {

  public final String title;

  public final String market;

  public final String name;

  public final String category;

  public final BigDecimal currentValue;

  public final int quantity;

  public CartItemDto(String title, String market, String name, String category,
                     BigDecimal currentValue, int quantity) {
    this.title = title;
    this.market = market;
    this.name = name;
    this.category = category;
    this.currentValue = currentValue;
    this.quantity = quantity;
  }
}

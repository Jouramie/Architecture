package ca.ulaval.glo4003.domain.cart;

import java.util.Objects;

public class CartItem {
  public String title;
  public int quantity;

  public CartItem(String title, int quantity) {
    this.title = title;
    this.quantity = quantity;
  }

  public CartItem(CartItem item) {
    title = item.title;
    quantity = item.quantity;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CartItem)) {
      return false;
    }

    CartItem otherItem = (CartItem) other;
    return otherItem.title.equals(title) && otherItem.quantity == quantity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, quantity);
  }
}


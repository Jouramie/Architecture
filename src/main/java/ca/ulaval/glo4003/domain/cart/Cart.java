package ca.ulaval.glo4003.domain.cart;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cart {
  private final Map<String, CartItem> items;

  public Cart() {
    items = new HashMap<>();
  }

  public void add(String title, int numStocks) {
    int newQty = getQty(title) + numStocks;
    if (newQty > 0) {
      items.put(title, new CartItem(title, newQty));
    }
  }

  public void remove(String title) {
    items.remove(title);
  }

  public void empty() {
    items.clear();
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public int getQty(String title) {
    if (items.containsKey(title)) {
      return items.get(title).quantity;
    }

    return 0;
  }

  public Collection<CartItem> getItems() {
    return items.values().stream().map(CartItem::new).collect(toList());
  }
}

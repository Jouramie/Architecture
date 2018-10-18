package ca.ulaval.glo4003.domain.cart;

import ca.ulaval.glo4003.domain.stock.StockCollection;
import java.util.ArrayList;
import java.util.List;

public class Cart {
  private StockCollection items;

  public Cart() {
    items = new StockCollection();
  }

  public void add(String title, int addedQuantity) {
    items = items.add(title, addedQuantity);
  }

  public void update(String title, int newQuantity) {
    items = items.update(title, newQuantity);
  }

  public void removeAll(String title) {
    items = items.removeAll(title);
  }

  public void empty() {
    items = items.empty();
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public int getQuantity(String title) {
    return items.getQuantity(title);
  }

  public List<CartItem> getItems() {
    List<CartItem> cartItems = new ArrayList<>();
    for (String title : items.getStocks()) {
      int quantity = items.getQuantity(title);
      cartItems.add(new CartItem(title, quantity));
    }
    return cartItems;
  }
}

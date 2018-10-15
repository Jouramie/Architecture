package ca.ulaval.glo4003.domain.cart;

import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Cart {
  private final StockCollection items;

  public Cart() {
    items = new StockCollection();
  }

  public void add(String title, int addedQuantity) {
    if (addedQuantity > 0) {
      items.add(title, addedQuantity);
    }
  }

  public void update(String title, int newQuantity) throws StockNotFoundException {
    checkIfStockInCart(title);
    if (newQuantity > 0) {
      items.update(title, newQuantity);
    }
  }

  public void removeAll(String title) {
    items.removeAll(title);
  }

  public void empty() {
    items.empty();
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public int getQuantity(String title) {
    return items.getQuantity(title);
  }

  private void checkIfStockInCart(String title) throws StockNotFoundException {
    if (!items.contains(title)) {
      throw new StockNotFoundException(title);
    }
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

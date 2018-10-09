package ca.ulaval.glo4003.domain.cart;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
  private final Map<String, CartItem> items;

  public Cart() {
    items = new HashMap<>();
  }

  public void add(String title, int addedQuantity) {
    int newQuantity = getQuantity(title) + addedQuantity;
    if (newQuantity > 0) {
      items.put(title, new CartItem(title, newQuantity));
    }
  }

  public void update(String title, int newQuantity) throws StockNotFoundException {
    checkIfStockInCart(title);
    if (newQuantity > 0) {
      items.put(title, new CartItem(title, newQuantity));
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

  public int getQuantity(String title) {
    if (items.containsKey(title)) {
      return items.get(title).quantity;
    }

    return 0;
  }

  private void checkIfStockInCart(String title) throws StockNotFoundException {
    if (!items.containsKey(title)) {
      throw new StockNotFoundException(title);
    }
  }

  public List<CartItem> getItems() {
    return items.values().stream().map(CartItem::new).collect(toList());
  }
}

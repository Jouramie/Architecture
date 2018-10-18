package ca.ulaval.glo4003.domain.cart;

import ca.ulaval.glo4003.domain.stock.StockCollection;

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

  public StockCollection getItems() {
    return items;
  }
}

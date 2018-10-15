package ca.ulaval.glo4003.domain.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockCollection {
  private final Map<String, Integer> stocks;

  public StockCollection() {
    stocks = new HashMap<>();
  }

  public boolean contains(String title) {
    return stocks.containsKey(title);
  }

  public void add(String title, int addedQuantity) {
    stocks.put(title, getQuantity(title) + addedQuantity);
  }

  public int getQuantity(String title) {
    int quantity = 0;

    if (stocks.containsKey(title)) {
      quantity = stocks.get(title);
    }

    return quantity;
  }

  public void update(String title, int quantity) {
    stocks.put(title, quantity);
  }

  public List<String> getStocks() {
    return new ArrayList<>(stocks.keySet());
  }

  public void remove(String title, int quantity) {
    stocks.put(title, getQuantity(title) - quantity);
  }

  public void removeAll(String title) {
    stocks.remove(title);
  }

  public boolean isEmpty() {
    return stocks.isEmpty();
  }

  public void empty() {
    stocks.clear();
  }
}

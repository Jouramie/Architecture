package ca.ulaval.glo4003.domain.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StockCollection {
  private final Map<String, Integer> stocks;

  public StockCollection() {
    stocks = new HashMap<>();
  }

  private StockCollection(Map<String, Integer> stocks) {
    this.stocks = stocks;
  }

  public boolean contains(String title) {
    return stocks.containsKey(title);
  }

  public StockCollection add(String title, int addedQuantity) {
    if (addedQuantity < 0) {
      throw new IllegalArgumentException();
    }

    Map<String, Integer> newMap = new HashMap<>(stocks);

    if (addedQuantity != 0) {
      newMap.put(title, getQuantity(title) + addedQuantity);
    }

    return new StockCollection(newMap);
  }

  public int getQuantity(String title) {
    return Optional.ofNullable(stocks.get(title)).orElse(0);
  }

  public StockCollection update(String title, int quantity) {
    if (!stocks.containsKey(title) || quantity < 0) {
      throw new IllegalArgumentException();
    }

    Map<String, Integer> newMap = new HashMap<>(stocks);

    if (quantity == 0) {
      newMap.remove(title);
    } else {
      newMap.put(title, quantity);
    }

    return new StockCollection(newMap);
  }

  public List<String> getStocks() {
    return new ArrayList<>(stocks.keySet());
  }

  public StockCollection remove(String title, int quantity) {
    if (!stocks.containsKey(title) || quantity < 0) {
      throw new IllegalArgumentException();
    }

    Map<String, Integer> newMap = new HashMap<>(stocks);

    if (getQuantity(title) - quantity < 0) {
      throw new RuntimeException("Removed quantity is higher than remaining quantity");
    } else if (getQuantity(title) - quantity == 0) {
      newMap.remove(title);
    } else {
      newMap.put(title, getQuantity(title) - quantity);
    }

    return new StockCollection(newMap);
  }

  public StockCollection removeAll(String title) {
    Map<String, Integer> newMap = new HashMap<>(stocks);

    newMap.remove(title);

    return new StockCollection(newMap);
  }

  public boolean isEmpty() {
    return stocks.isEmpty();
  }

  public StockCollection empty() {
    Map<String, Integer> newMap = new HashMap<>(stocks);

    newMap.clear();

    return new StockCollection(newMap);
  }
}

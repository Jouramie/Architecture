package ca.ulaval.glo4003.domain.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StockCollection {
  private final Map<String, Integer> stocks;
  private final StockRepository stockRepository;

  public StockCollection(StockRepository stockRepository) {
    this(new HashMap<>(), stockRepository);
  }

  private StockCollection(Map<String, Integer> stocks, StockRepository stockRepository) {
    this.stocks = stocks;
    this.stockRepository = stockRepository;
  }

  public boolean contains(String title) {
    return stocks.containsKey(title);
  }

  public StockCollection add(String title, int addedQuantity) {
    if (!stockRepository.doesStockExist(title) || addedQuantity < 0) {
      throw new IllegalArgumentException();
    }

    Map<String, Integer> newMap = new HashMap<>(stocks);

    if (addedQuantity != 0) {
      newMap.put(title, getQuantity(title) + addedQuantity);
    }

    return new StockCollection(newMap, stockRepository);
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

    return new StockCollection(newMap, stockRepository);
  }

  public List<String> getTitles() {
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

    return new StockCollection(newMap, stockRepository);
  }

  public StockCollection removeAll(String title) {
    Map<String, Integer> newMap = new HashMap<>(stocks);

    newMap.remove(title);

    return new StockCollection(newMap, stockRepository);
  }

  public boolean isEmpty() {
    return stocks.isEmpty();
  }

  public StockCollection empty() {
    return new StockCollection(stockRepository);
  }
}

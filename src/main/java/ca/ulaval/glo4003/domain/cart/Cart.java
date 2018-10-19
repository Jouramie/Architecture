package ca.ulaval.glo4003.domain.cart;

import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockRepository;

public class Cart {
  private StockCollection stocks;

  public Cart(StockRepository stockRepository) {
    stocks = new StockCollection(stockRepository);
  }

  public void add(String title, int addedQuantity) {
    stocks = stocks.add(title, addedQuantity);
  }

  public void update(String title, int newQuantity) {
    stocks = stocks.update(title, newQuantity);
  }

  public void removeAll(String title) {
    stocks = stocks.removeAll(title);
  }

  public void empty() {
    stocks = stocks.empty();
  }

  public boolean isEmpty() {
    return stocks.isEmpty();
  }

  public int getQuantity(String title) {
    return stocks.getQuantity(title);
  }

  public StockCollection getStocks() {
    return stocks;
  }
}

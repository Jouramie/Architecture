package ca.ulaval.glo4003.domain.cart;

import ca.ulaval.glo4003.domain.market.HaltedMarketException;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;
import ca.ulaval.glo4003.domain.user.exceptions.EmptyCartException;

public class Cart {
  private StockCollection stocks;

  public Cart() {
    stocks = new StockCollection();
  }

  public void add(String title, int addedQuantity, StockRepository stockRepository) {
    stocks = stocks.add(title, addedQuantity, stockRepository);
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

  public Transaction checkoutCart(TransactionFactory transactionFactory)
      throws StockNotFoundException, HaltedMarketException, EmptyCartException {

    checkIfCartIsEmpty();
    Transaction purchase = transactionFactory.createPurchase(stocks);

    empty();
    return purchase;
  }

  private void checkIfCartIsEmpty() throws EmptyCartException {
    if (isEmpty()) {
      throw new EmptyCartException();
    }
  }
}

package ca.ulaval.glo4003.domain.cart;

import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.exception.HaltedMarketException;
import ca.ulaval.glo4003.domain.market.exception.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.state.Market;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.exception.StockNotFoundException;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionFactory;

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

  public Transaction checkout(TransactionFactory transactionFactory, MarketRepository marketRepository)
      throws StockNotFoundException, HaltedMarketException, EmptyCartException {

    ensureCartIsNotEmpty();
    ensureMarketsOfStocksAreNotHalted(marketRepository);
    return transactionFactory.createPurchase(stocks);
  }

  private void ensureCartIsNotEmpty() throws EmptyCartException {
    if (isEmpty()) {
      throw new EmptyCartException();
    }
  }

  private void ensureMarketsOfStocksAreNotHalted(MarketRepository marketRepository)
      throws StockNotFoundException, HaltedMarketException {
    for (String title : stocks.getTitles()) {
      ensureMarketOfStockIsNotHalted(title, marketRepository);
    }
  }

  private void ensureMarketOfStockIsNotHalted(String title, MarketRepository marketRepository) throws HaltedMarketException, StockNotFoundException {
    try {
      Market market = marketRepository.findByStock(title);
      if (market.isHalted()) {
        throw new HaltedMarketException(market.getHaltMessage());
      }
    } catch (MarketNotFoundException e) {
      throw new StockNotFoundException(title);
    }
  }
}

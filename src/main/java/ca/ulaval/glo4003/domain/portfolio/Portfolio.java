package ca.ulaval.glo4003.domain.portfolio;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionHistory;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {
  private final TransactionHistory transactionHistory;
  private StockCollection stocks;

  public Portfolio() {
    transactionHistory = new TransactionHistory();
    stocks = new StockCollection();
  }

  public void add(Transaction transaction, StockRepository stockRepository) {
    transactionHistory.save(transaction);

    for (TransactionItem item : transaction.items) {
      stocks = stocks.add(item.title, item.quantity, stockRepository);
    }
  }

  public int getQuantity(String title) {
    return stocks.getQuantity(title);
  }

  public MoneyAmount getCurrentTotalValue(StockRepository stockRepository) throws InvalidStockInPortfolioException {
    return getStockList(stockRepository).stream().map(this::getSubtotal)
        .reduce(MoneyAmount.zero(Currency.USD), MoneyAmount::add);
  }

  public StockCollection getStocks() {
    return stocks;
  }

  private MoneyAmount getSubtotal(Stock stock) {
    MoneyAmount currentValue = stock.getValue().getCurrentValue();
    int quantity = stocks.getQuantity(stock.getTitle());
    return currentValue.multiply(quantity);
  }

  private List<Stock> getStockList(StockRepository stockRepository) throws InvalidStockInPortfolioException {
    List<Stock> stockList = new ArrayList<>();
    for (String title : stocks.getTitles()) {
      try {
        stockList.add(stockRepository.findByTitle(title));
      } catch (StockNotFoundException e) {
        throw new InvalidStockInPortfolioException("Portfolio contains invalid stock with title " + title);
      }
    }
    return stockList;
  }
}

package ca.ulaval.glo4003.domain.portfolio;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionHistory;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class Portfolio {
  private final TransactionHistory transactionHistory;
  private StockCollection stocks;

  public Portfolio() {
    transactionHistory = new TransactionHistory();
    stocks = new StockCollection();
  }

  public TreeSet<HistoricalPortfolio> getHistory(LocalDate from, LocalDate now) {
    TreeSet<HistoricalPortfolio> result = new TreeSet<>();

    LocalDate currentDate = now;
    StockCollection currentStockCollection = stocks;

    while (currentDate.isAfter(from) || currentDate.isEqual(from)) {
      result.add(new HistoricalPortfolio(currentDate, currentStockCollection));
      currentStockCollection = rollbackTransactionsForDay(currentDate, currentStockCollection);
      currentDate = currentDate.minusDays(1);
    }

    return result;
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

  public String getMostIncreasingStockTitle(LocalDate from, StockRepository stockRepository)
      throws InvalidStockInPortfolioException, NoStockValueFitsCriteriaException {
    List<Stock> stockList = getStockList(stockRepository);
    BigDecimal highestVariation = BigDecimal.ZERO;
    String mostIncreasingStockTitle = null;
    for (Stock stock : stockList) {
      BigDecimal variation = computeStockValueVariation(from, stock);
      if (variation.compareTo(highestVariation) > 0) {
        highestVariation = variation;
        mostIncreasingStockTitle = stock.getTitle();
      }
    }
    return mostIncreasingStockTitle;
  }

  public String getMostDecreasingStockTitle(LocalDate from, StockRepository stockRepository)
      throws InvalidStockInPortfolioException, NoStockValueFitsCriteriaException {
    List<Stock> stockList = getStockList(stockRepository);
    BigDecimal lowestVariation = new BigDecimal(Double.MAX_VALUE);
    String mostDecreasingStockTitle = null;
    for (Stock stock : stockList) {
      BigDecimal variation = computeStockValueVariation(from, stock);
      if (variation.compareTo(lowestVariation) < 0) {
        lowestVariation = variation;
        mostDecreasingStockTitle = stock.getTitle();
      }
    }
    return mostDecreasingStockTitle;
  }

  private BigDecimal computeStockValueVariation(LocalDate from, Stock stock) throws NoStockValueFitsCriteriaException {
    MoneyAmount startAmount = stock.getValueHistory().getValueOnDay(from).getLatestValue();
    MoneyAmount currentAmount = stock.getValue().getLatestValue();
    return currentAmount.divide(startAmount);
  }

  private MoneyAmount getSubtotal(Stock stock) {
    MoneyAmount currentValue = stock.getValue().getLatestValue();
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

  private StockCollection rollbackTransactionsForDay(LocalDate date, StockCollection currentStockCollection) {
    TreeSet<Transaction> transactions = transactionHistory.getTransactions(date);
    Iterator<Transaction> it = transactions.descendingIterator();
    while (it.hasNext()) {
      Transaction transaction = it.next();
      currentStockCollection = rollbackTransaction(currentStockCollection, transaction);
    }
    return currentStockCollection;
  }

  private StockCollection rollbackTransaction(StockCollection stockCollection, Transaction transaction) {
    StockCollection currentCollection = stockCollection;

    for (TransactionItem item : transaction.items) {
      currentCollection = currentCollection.remove(item.title, item.quantity);
    }

    return currentCollection;
  }
}

package ca.ulaval.glo4003.domain.portfolio;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {
  private static final Currency DEFAULT_CURRENCY = new Currency("USD", new BigDecimal(1));
  private final StockRepository stockRepository;
  private StockCollection stocks;

  public Portfolio(StockRepository stockRepository) {
    stocks = new StockCollection();
    this.stockRepository = stockRepository;
  }

  public void add(String title, int quantity) throws StockNotFoundException {
    checkIfStockExists(title);
    stocks = stocks.add(title, quantity);
  }

  public int getQuantity(String title) {
    return stocks.getQuantity(title);
  }

  public MoneyAmount getCurrentTotalValue() throws InvalidStockInPortfolioException {
    List<Stock> stockList = getStockList();

    Currency currency = getFirstStockCurrencyOrDefault(stockList);
    MoneyAmount currentTotalValue = MoneyAmount.zero(currency);
    for (Stock stock : stockList) {
      currentTotalValue = currentTotalValue.add(getSubtotal(stock));
    }

    return currentTotalValue;
  }

  private void checkIfStockExists(String title) throws StockNotFoundException {
    stockRepository.getByTitle(title);
  }

  private MoneyAmount getSubtotal(Stock stock) {
    MoneyAmount currentValue = stock.getValue().getCurrentValue();
    int quantity = stocks.getQuantity(stock.getTitle());
    return currentValue.multiply(quantity);
  }

  private List<Stock> getStockList() throws InvalidStockInPortfolioException {
    List<Stock> stockList = new ArrayList<>();
    for (String title : stocks.getStocks()) {
      try {
        stockList.add(stockRepository.getByTitle(title));
      } catch (StockNotFoundException e) {
        throw new InvalidStockInPortfolioException("Portfolio contains invalid stock with title " + title);
      }
    }
    return stockList;
  }

  private Currency getFirstStockCurrencyOrDefault(List<Stock> stockList) {
    if (stockList.isEmpty()) {
      return DEFAULT_CURRENCY;
    }

    return stockList.get(0).getCurrency();
  }
}

package ca.ulaval.glo4003.domain.portfolio;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.math.BigDecimal;
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

  public MoneyAmount getCurrentTotalValue() {
    List<Stock> stockList = stocks.getStocks().stream().map(this::tryGetStockByTitle)
        .collect(toList());

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

  private Stock tryGetStockByTitle(String title) {
    Stock stock;

    try {
      stock = stockRepository.getByTitle(title);
    } catch (StockNotFoundException e) {
      throw new RuntimeException();
    }

    return stock;
  }

  private Currency getFirstStockCurrencyOrDefault(List<Stock> stockList) {
    Currency currency = DEFAULT_CURRENCY;

    if (!stockList.isEmpty()) {
      currency = stockList.get(0).getValue().getCurrentValue().getCurrency();
    }

    return currency;
  }
}

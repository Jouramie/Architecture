package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Stock {
  private final String title;
  private final String name;
  private final String category;
  private final MarketId marketId;
  private final StockHistory valueHistory;
  private boolean closed;

  public Stock(String title, String name, String category, MarketId marketId,
               StockHistory valueHistory) {
    this.title = title;
    this.name = name;
    this.category = category;
    this.marketId = marketId;
    this.valueHistory = valueHistory;
    closed = true; // TODO vérifier si c'est legit
  }

  public String getTitle() {
    return title;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public MarketId getMarketId() {
    return marketId;
  }

  public StockHistory getValueHistory() {
    return valueHistory;
  }

  public Currency getCurrency() {
    return getValue().getLatestValue().getCurrency();
  }

  public boolean isClosed() {
    return closed;
  }

  public synchronized void updateValue(BigDecimal variation) {
    valueHistory.updateCurrentValue(variation);
  }

  public synchronized StockValue getValue() {
    return valueHistory.getLatestHistoricalValue().value;
  }

  public synchronized StockValue getLatestValueOnDate(LocalDate date) throws NoStockValueFitsCriteriaException {
    if (date.isAfter(valueHistory.getLatestHistoricalValue().date)) {
      return valueHistory.getLatestHistoricalValue().value;
    }

    return valueHistory.getValueOnDay(date).orElseThrow(NoStockValueFitsCriteriaException::new);
  }

  public BigDecimal computeStockValueVariation(LocalDate from) throws NoStockValueFitsCriteriaException {
    MoneyAmount startAmount = getLatestValueOnDate(from).getLatestValue();
    MoneyAmount currentAmount = getValue().getLatestValue();
    return currentAmount.divide(startAmount);
  }

  public synchronized void open() {
    closed = false;

    MoneyAmount startValue = getValue().getLatestValue();
    StockValue newStockValue = StockValue.create(startValue);
    valueHistory.addNextValue(newStockValue);
  }

  public synchronized void close() {
    closed = true;
  }
}

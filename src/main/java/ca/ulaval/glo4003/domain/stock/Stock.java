package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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
    closed = true;
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
    return valueHistory.getLatestValue();
  }

  public synchronized Optional<StockValue> getValueOnDate(LocalDate date) {
    if (valueHistory.isAfterLatestValue(date)) {
      return Optional.of(valueHistory.getLatestValue());
    }

    return valueHistory.getValueOnDay(date);
  }

  public BigDecimal computeStockValueVariation(LocalDate from) throws NoStockValueFitsCriteriaException {
    MoneyAmount startAmount = getValueOnDate(from).orElseThrow(NoStockValueFitsCriteriaException::new).getLatestValue();
    MoneyAmount currentAmount = getValue().getLatestValue();
    return currentAmount.divide(startAmount);
  }

  public synchronized void open() {
    closed = false;
    valueHistory.addNextValue();
  }

  public synchronized void close() {
    closed = true;
  }
}

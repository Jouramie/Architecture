package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class Stock {
  private final String title;
  private final String name;
  private final String category;
  private final MarketId marketId;
  private final StockValueHistory valueHistory;

  public Stock(String title, String name, String category, MarketId marketId,
               StockValueHistory valueHistory) {
    this.title = title;
    this.name = name;
    this.category = category;
    this.marketId = marketId;
    this.valueHistory = valueHistory;
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

  public StockValueHistory getValueHistory() {
    return valueHistory;
  }

  public synchronized void updateValue(double variation) {
    MoneyAmount moneyVariation = new MoneyAmount(variation, getValue().getCurrentValue().getCurrency());
    MoneyAmount newMoneyAmount = getValue().getCurrentValue().add(moneyVariation);
    getValue().setValue(newMoneyAmount);
  }

  public synchronized StockValue getValue() {
    return valueHistory.getLatestValue().value;
  }

  public synchronized void open() {
    MoneyAmount startValue = getValue().getCloseValue();
    StockValue newStockValue = new StockValue(startValue);

    valueHistory.addNextValue(newStockValue);
  }

  public synchronized void close() {
    getValue().close();
  }
}

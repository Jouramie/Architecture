package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class Stock {
  private final String title;
  private final String name;
  private final MarketId marketId;
  private final StockValue value;

  public Stock(String title, String name, MarketId marketId, MoneyAmount startValue) {
    this.title = title;
    this.name = name;
    this.marketId = marketId;
    value = new StockValue(startValue);
  }

  public String getTitle() {
    return title;
  }

  public String getName() {
    return name;
  }

  public MarketId getMarketId() {
    return marketId;
  }

  public synchronized void updateValue(double variation) {
    MoneyAmount moneyVariation = new MoneyAmount(variation, value.getCurrentValue().getCurrency());
    MoneyAmount newMoneyAmount = value.getCurrentValue().add(moneyVariation);
    value.setValue(newMoneyAmount);
  }

  public synchronized StockValue getValue() {
    return value;
  }

  public synchronized void close() {
    value.close();
  }
}

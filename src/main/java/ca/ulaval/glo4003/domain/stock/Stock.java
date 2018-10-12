package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class Stock {
  private final String title;
  private final String name;
  private final String category;
  private final MarketId marketId;
  private final StockValueHistorian valueHistorian;

  public Stock(String title, String name, String category, MarketId marketId,
               StockValueHistorian valueHistorian) {
    this.title = title;
    this.name = name;
    this.category = category;
    this.marketId = marketId;
    this.valueHistorian = valueHistorian;
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

  public StockValueHistorian getValueHistorian() {
    return valueHistorian;
  }

  public synchronized void updateValue(double variation) {
    MoneyAmount moneyVariation = new MoneyAmount(variation, getValue().getCurrentValue().getCurrency());
    MoneyAmount newMoneyAmount = getValue().getCurrentValue().add(moneyVariation);
    getValue().setValue(newMoneyAmount);
  }

  public synchronized StockValue getValue() {
    return valueHistorian.getLatestValue().value;
  }

  public synchronized void open() {
    MoneyAmount startValue = getValue().getCloseValue();
    StockValue newStockValue = new StockValue(startValue);

    valueHistorian.addNextValue(newStockValue);
  }

  public synchronized void close() {
    getValue().close();
  }
}

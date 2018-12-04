package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class StockValueBuilder {
  public static final MoneyAmount DEFAULT_OPEN_VALUE = new MoneyAmount(10);
  public static final MoneyAmount DEFAULT_LATEST_VALUE = new MoneyAmount(12);
  public static final MoneyAmount DEFAULT_MAXIMUM_VALUE = new MoneyAmount(20);

  private MoneyAmount openValue = DEFAULT_OPEN_VALUE;
  private MoneyAmount latestValue = DEFAULT_LATEST_VALUE;
  private MoneyAmount maximumValue = DEFAULT_MAXIMUM_VALUE;

  public StockValueBuilder withOpenValue(double openValue) {
    this.openValue = new MoneyAmount(openValue);
    return this;
  }

  public StockValueBuilder withLatestValue(double latestValue) {
    this.latestValue = new MoneyAmount(latestValue);
    return this;
  }

  public StockValueBuilder withLatestValue(MoneyAmount latestValue) {
    this.latestValue = latestValue;
    return this;
  }

  public StockValueBuilder withMaximumValue(double maximumValue) {
    this.maximumValue = new MoneyAmount(maximumValue);
    return this;
  }

  public StockValueBuilder withMaximumValue(MoneyAmount maximumValue) {
    this.maximumValue = maximumValue;
    return this;
  }

  public StockValueBuilder withAllValue(double value) {
    this.openValue = new MoneyAmount(value);
    this.latestValue = new MoneyAmount(value);
    this.maximumValue = new MoneyAmount(value);
    return this;
  }

  public StockValueBuilder withAllValue(MoneyAmount value) {
    this.openValue = value;
    this.latestValue = value;
    this.maximumValue = value;
    return this;
  }

  public StockValue build() {
    return new StockValue(openValue, latestValue, maximumValue);
  }
}

package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;

public class StockValue {
  private final MoneyAmount latestValue;
  private final MoneyAmount openValue;
  private final MoneyAmount maximumValue;
  private final boolean isClosed;

  private StockValue(MoneyAmount openValue, MoneyAmount latestValue, MoneyAmount maximumValue, boolean isClosed) {
    this.openValue = openValue;
    this.latestValue = latestValue;
    this.maximumValue = maximumValue;
    this.isClosed = isClosed;
  }

  public static StockValue createOpen(MoneyAmount startValue) {
    return new StockValue(startValue, startValue, startValue, false);
  }

  public static StockValue createOpen(MoneyAmount startValue, MoneyAmount latestValue, MoneyAmount closeValue) {
    return new StockValue(startValue, latestValue, closeValue, false);
  }

  public static StockValue createClosed(MoneyAmount startValue, MoneyAmount latestValue, MoneyAmount closeValue) {
    return new StockValue(startValue, latestValue, closeValue, true);
  }

  public MoneyAmount getLatestValue() {
    return latestValue;
  }

  public MoneyAmount getOpenValue() {
    return openValue;
  }

  public MoneyAmount getMaximumValue() {
    return maximumValue;
  }

  public boolean isClosed() {
    return isClosed;
  }

  public StockValue updateValue(BigDecimal variation) {
    return setValue(getLatestValue().add(variation));
  }

  public StockValue setValue(MoneyAmount currentValue) {
    if (isClosed()) {
      return createOpen(currentValue);
    }

    if (currentValue.compareTo(maximumValue) > 0) {
      return createOpen(openValue, currentValue, currentValue);
    }

    return new StockValue(openValue, currentValue, maximumValue, false);
  }

  public StockValue close() {
    return new StockValue(openValue, latestValue, maximumValue, true);
  }
}

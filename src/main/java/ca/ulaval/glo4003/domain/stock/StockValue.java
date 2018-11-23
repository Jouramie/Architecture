package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;

public class StockValue {
  private MoneyAmount latestValue;
  private MoneyAmount openValue;
  private MoneyAmount maximumValue;
  private boolean isClosed;

  public StockValue(MoneyAmount startValue) {
    latestValue = startValue;
    openValue = startValue;
    maximumValue = startValue;
    isClosed = false;
  }

  public StockValue(MoneyAmount openValue, MoneyAmount closeValue, MoneyAmount maximumValue) {
    latestValue = closeValue;
    this.openValue = openValue;
    this.maximumValue = maximumValue;
    isClosed = true;
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

  public void updateValue(BigDecimal variation) {
    setValue(getLatestValue().add(variation));
  }

  public void setValue(MoneyAmount currentValue) {
    if (isClosed()) {
      openValue = currentValue;
      isClosed = false;
    }

    if (currentValue.toUsd().compareTo(maximumValue.toUsd()) > 0) {
      maximumValue = currentValue;
    }

    latestValue = currentValue;
  }

  public void close() {
    isClosed = true;
  }
}

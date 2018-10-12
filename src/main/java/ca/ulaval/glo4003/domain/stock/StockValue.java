package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class StockValue {
  private MoneyAmount currentValue;
  private MoneyAmount openValue;
  private MoneyAmount closeValue;
  private MoneyAmount maximumValue;

  public StockValue(MoneyAmount startValue) {
    currentValue = startValue;
    openValue = startValue;
    maximumValue = startValue;
    closeValue = null;
  }

  public StockValue(MoneyAmount openValue, MoneyAmount closeValue, MoneyAmount maximumValue) {
    currentValue = closeValue;
    this.openValue = openValue;
    this.closeValue = closeValue;
    this.maximumValue = maximumValue;
  }

  public MoneyAmount getCurrentValue() {
    return currentValue;
  }

  public MoneyAmount getOpenValue() {
    return openValue;
  }

  public MoneyAmount getCloseValue() {
    return closeValue;
  }

  public MoneyAmount getMaximumValue() {
    return maximumValue;
  }

  boolean isClosed() {
    return closeValue != null;
  }

  public void setValue(MoneyAmount currentValue) {
    if (isClosed()) {
      openValue = currentValue;
      closeValue = null;
    }

    if (currentValue.toUsd().compareTo(maximumValue.toUsd()) > 0) {
      maximumValue = currentValue;
    }

    this.currentValue = currentValue;
  }

  public void close() {
    closeValue = currentValue;
  }
}

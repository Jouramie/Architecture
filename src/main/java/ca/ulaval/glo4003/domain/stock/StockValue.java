package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class StockValue {
  private MoneyAmount currentValue;
  private MoneyAmount openValue;
  private MoneyAmount closeValue;

  StockValue(MoneyAmount startValue) {
    currentValue = startValue;
    openValue = startValue;
    closeValue = startValue;
  }

  public MoneyAmount getCurrentValue() {
    return currentValue;
  }

  MoneyAmount getOpenValue() {
    return openValue;
  }

  MoneyAmount getCloseValue() {
    return closeValue;
  }

  boolean isClosed() {
    return closeValue != null;
  }

  public void setValue(MoneyAmount currentValue) {
    if (closeValue != null) {
      openValue = currentValue;
      closeValue = null;
    }

    this.currentValue = currentValue;
  }

  public void close() {
    closeValue = currentValue;
  }
}

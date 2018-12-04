package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;
import java.util.Objects;

public class StockValue {
  private final MoneyAmount latestValue;
  private final MoneyAmount openValue;
  private final MoneyAmount maximumValue;

  public StockValue(MoneyAmount openValue, MoneyAmount latestValue, MoneyAmount maximumValue) {
    this.openValue = openValue;
    this.latestValue = latestValue;
    this.maximumValue = maximumValue;
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

  public StockValue updateCurrentValue(BigDecimal variation) {
    MoneyAmount newLatestValue = latestValue.add(variation);
    if (newLatestValue.compareTo(maximumValue) > 0) {
      return new StockValue(openValue, newLatestValue, newLatestValue);
    }

    return new StockValue(openValue, newLatestValue, maximumValue);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockValue that = (StockValue) o;
    return Objects.equals(latestValue, that.latestValue)
        && Objects.equals(openValue, that.openValue)
        && Objects.equals(maximumValue, that.maximumValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(latestValue, openValue, maximumValue);
  }
}

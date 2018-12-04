package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;

public class StockValue {
  private final MoneyAmount latestValue;
  private final MoneyAmount openValue;
  private final MoneyAmount maximumValue;

  private StockValue(MoneyAmount openValue, MoneyAmount latestValue, MoneyAmount maximumValue) {
    this.openValue = openValue;
    this.latestValue = latestValue;
    this.maximumValue = maximumValue;
  }

  public static StockValue create(MoneyAmount startValue) {
    return new StockValue(startValue, startValue, startValue);
  }

  public static StockValue create(MoneyAmount startValue, MoneyAmount latestValue, MoneyAmount closeValue) {
    return new StockValue(startValue, latestValue, closeValue);
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

  public StockValue updateValue(BigDecimal variation) {
    return setValue(getLatestValue().add(variation));
  }

  public StockValue setValue(MoneyAmount currentValue) {
    if (currentValue.compareTo(maximumValue) > 0) {
      return create(openValue, currentValue, currentValue);
    }

    return new StockValue(openValue, currentValue, maximumValue);
  }
}

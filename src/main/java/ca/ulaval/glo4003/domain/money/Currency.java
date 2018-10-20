package ca.ulaval.glo4003.domain.money;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Currency {
  public static final Currency USD = new Currency("USD", BigDecimal.ONE);

  private final String name;
  private final BigDecimal rateToUsd;

  public Currency(String name, BigDecimal rateToUsd) {
    this.name = name;
    this.rateToUsd = rateToUsd;
  }

  public String getName() {
    return name;
  }

  public MoneyAmount convert(MoneyAmount amount) {
    BigDecimal factor = amount.getCurrency().rateToUsd.divide(rateToUsd, MathContext.DECIMAL64);
    return new MoneyAmount(amount.getAmount().multiply(factor)
        .setScale(2, RoundingMode.HALF_EVEN), this);
  }

  BigDecimal toUsd(BigDecimal amount) {
    return amount.multiply(rateToUsd).setScale(2, RoundingMode.HALF_EVEN);
  }
}

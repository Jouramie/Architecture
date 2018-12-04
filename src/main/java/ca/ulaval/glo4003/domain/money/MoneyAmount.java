package ca.ulaval.glo4003.domain.money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyAmount implements Comparable<MoneyAmount> {

  public static final MoneyAmount ZERO = zero(Currency.USD);

  private final BigDecimal amount;
  private final Currency currency;

  public MoneyAmount(double amount) {
    this(amount, Currency.USD);
  }

  public MoneyAmount(BigDecimal amount) {
    this(amount, Currency.USD);
  }

  public MoneyAmount(double amount, Currency currency) {
    this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN);
    this.currency = currency;
  }

  public MoneyAmount(BigDecimal amount, Currency currency) {
    this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    this.currency = currency;
  }

  public static MoneyAmount zero(Currency currency) {
    return new MoneyAmount(0, currency);
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public MoneyAmount add(MoneyAmount otherAmount) {
    MoneyAmount convertedAmount = currency.convert(otherAmount);
    return new MoneyAmount(amount.add(convertedAmount.getAmount()), getCurrency());
  }

  public MoneyAmount add(BigDecimal variation) {
    MoneyAmount variationMoneyAmount = new MoneyAmount(variation, getCurrency());
    return add(variationMoneyAmount);
  }

  public MoneyAmount subtract(MoneyAmount otherAmount) {
    MoneyAmount convertedAmount = currency.convert(otherAmount);
    return new MoneyAmount(amount.subtract(convertedAmount.getAmount()), getCurrency());
  }

  public MoneyAmount multiply(int multiplier) {
    return new MoneyAmount(amount.multiply(new BigDecimal(multiplier)), getCurrency());
  }

  public BigDecimal divide(MoneyAmount other) {
    return amount.divide(other.amount, RoundingMode.HALF_EVEN);
  }

  public BigDecimal toUsd() {
    return currency.toUsd(amount);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof MoneyAmount)) {
      return false;
    }

    MoneyAmount otherAmount = (MoneyAmount) other;
    return getAmount().equals(otherAmount.getAmount())
        && getCurrency().equals(otherAmount.getCurrency());
  }

  @Override
  public int hashCode() {
    return getAmount().hashCode() ^ getCurrency().hashCode();
  }

  public boolean isGreaterThan(MoneyAmount other) {
    return toUsd().compareTo(other.toUsd()) > 0;
  }

  public boolean isLessThan(MoneyAmount other) {
    return toUsd().compareTo(other.toUsd()) < 0;
  }

  @Override
  public int compareTo(MoneyAmount maximumValue) {
    return toUsd().compareTo(maximumValue.toUsd());
  }
}

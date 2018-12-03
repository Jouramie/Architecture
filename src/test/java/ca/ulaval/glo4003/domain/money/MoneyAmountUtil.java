package ca.ulaval.glo4003.domain.money;

public class MoneyAmountUtil {

  public static Currency DEFAULT_CURRENCY = Currency.USD;

  public static MoneyAmount of(double value) {
    return new MoneyAmount(value, Currency.USD);
  }

  public static MoneyAmount of(double value, Currency currency) {
    return new MoneyAmount(value, currency);
  }
}

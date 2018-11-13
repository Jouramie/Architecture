package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import java.math.BigDecimal;

public class TransactionItemBuilder {
  public static final double DEFAULT_AMOUNT_VALUE = 40.00;
  public static final Currency DEFAULT_CURRENCY = new Currency("CAD", new BigDecimal(0.77));

  public static final String DEFAULT_TITLE = "title";
  public static final int DEFAULT_QUANTITY = 1;
  public static final MoneyAmount DEFAULT_AMOUNT = new MoneyAmount(DEFAULT_AMOUNT_VALUE, DEFAULT_CURRENCY);

  private String title = DEFAULT_TITLE;
  private int quantity = DEFAULT_QUANTITY;
  private MoneyAmount amount = DEFAULT_AMOUNT;

  public TransactionItemBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public TransactionItemBuilder withQuantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public TransactionItemBuilder withAmount(MoneyAmount amount) {
    this.amount = amount;
    return this;
  }

  public TransactionItem build() {
    return new TransactionItem(title, quantity, amount);
  }
}

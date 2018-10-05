package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import java.math.BigDecimal;

public class TransactionItemBuilder {
  private static final double DEFAULT_LAST_OPEN_VALUE = 40.00;
  private static final Currency DEFAULT_CURRENCY = new Currency("CAD", new BigDecimal(0.77));

  private static final String DEFAULT_STOCK_ID = "title";
  private static final int DEFAULT_QUANTITY = 1;
  private static final MoneyAmount DEFAULT_AMOUNT = new MoneyAmount(DEFAULT_LAST_OPEN_VALUE, DEFAULT_CURRENCY);

  public TransactionItem build() {
    return new TransactionItem(DEFAULT_STOCK_ID, DEFAULT_QUANTITY, DEFAULT_AMOUNT);
  }
}

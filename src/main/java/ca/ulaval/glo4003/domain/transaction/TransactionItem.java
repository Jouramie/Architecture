package ca.ulaval.glo4003.domain.transaction;

import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class TransactionItem {
  public final int quantity;
  public final String stockId;
  public final MoneyAmount amount;

  public TransactionItem(String stockId, int quantity, MoneyAmount amount) {
    this.stockId = stockId;
    this.quantity = quantity;
    this.amount = amount;
  }

  MoneyAmount getTotal() {
    return amount.multiply(quantity);
  }
}

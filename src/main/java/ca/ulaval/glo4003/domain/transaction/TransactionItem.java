package ca.ulaval.glo4003.domain.transaction;

import ca.ulaval.glo4003.domain.money.MoneyAmount;

public class TransactionItem {
  public final String title;
  public final int quantity;
  public final MoneyAmount amount;

  public TransactionItem(String title, int quantity, MoneyAmount amount) {
    this.title = title;
    this.quantity = quantity;
    this.amount = amount;
  }

  MoneyAmount calculateTotal() {
    return amount.multiply(quantity);
  }
}

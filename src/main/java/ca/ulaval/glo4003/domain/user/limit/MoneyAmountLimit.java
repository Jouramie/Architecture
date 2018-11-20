package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import java.time.LocalDateTime;

public class MoneyAmountLimit extends Limit {
  public final MoneyAmount amount;

  public MoneyAmountLimit(LocalDateTime start, LocalDateTime end, MoneyAmount amount) {
    super(start, end);
    this.amount = amount;
  }

  @Override
  public boolean doesTransactionExceedLimit(Transaction transaction) {
    return isTransactionInsideLimitTimeSpan(transaction)
        && transaction.calculateTotal().isGreaterThan(amount);
  }
}

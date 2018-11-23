package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import java.time.LocalDateTime;

public class MoneyAmountLimit extends TemporaryLimit {
  public final MoneyAmount moneyAmount;

  public MoneyAmountLimit(LocalDateTime begin, LocalDateTime end, MoneyAmount moneyAmount) {
    super(begin, end);
    this.moneyAmount = moneyAmount;
  }

  @Override
  protected boolean isSpecificCriteriaExceeded(Transaction transaction) {
    return transaction.calculateTotal().isGreaterThan(moneyAmount);
  }
}

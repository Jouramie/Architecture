package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import java.time.LocalDateTime;

public abstract class TemporaryLimit extends Limit {
  public final LocalDateTime start;
  public final LocalDateTime end;

  public TemporaryLimit(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public void checkIfTransactionExceed(Transaction transaction) throws TransactionLimitExceededExeption {
    if (isTransactionInsideLimitTimeSpan(transaction)
        && isSpecificCriteriaExceeded(transaction)) {
      throw new TransactionLimitExceededExeption();
    }
  }

  private boolean isTransactionInsideLimitTimeSpan(Transaction transaction) {
    return transaction.timestamp.isEqual(start)
        || transaction.timestamp.isEqual(end)
        || (transaction.timestamp.isAfter(start)
        && transaction.timestamp.isBefore(end));
  }

  protected abstract boolean isSpecificCriteriaExceeded(Transaction transaction);
}

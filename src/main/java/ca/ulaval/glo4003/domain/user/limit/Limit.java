package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import java.time.LocalDateTime;

public abstract class Limit {
  public final LocalDateTime start;
  public final LocalDateTime end;

  public Limit(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
  }

  public abstract boolean canProcessTransaction(Transaction transaction);

  boolean isTransactionOutsideLimitTimeSpan(Transaction transaction) {
    return transaction.timestamp.isBefore(start)
        || transaction.timestamp.isAfter(end);
  }
}

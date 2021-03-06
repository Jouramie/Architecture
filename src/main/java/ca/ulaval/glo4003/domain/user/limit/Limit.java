package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;

public abstract class Limit {
  public abstract void ensureTransactionIsUnderLimit(Transaction transaction) throws TransactionLimitExceededExeption;
}

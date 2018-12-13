package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;

public class NullLimit extends Limit {
  @Override
  public void ensureTransactionIsUnderLimit(Transaction transaction) {
  }
}

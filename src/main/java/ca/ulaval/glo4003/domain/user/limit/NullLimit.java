package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;

public class NullLimit extends Limit {

  public NullLimit() {
    super(null, null);
  }

  @Override
  public boolean doesTransactionExceedLimit(Transaction transaction) {
    return false;
  }
}

package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionBuilder;
import org.junit.Test;

public class NullLimitTest {

  private static final Transaction SOME_TRANSACTION = new TransactionBuilder().build();

  @Test
  public void whenEnsureTransactionIsUnderLimit_thenNoExceptionIsThrow() {
    NullLimit limit = new NullLimit();

    limit.ensureTransactionIsUnderLimit(SOME_TRANSACTION);
  }
}

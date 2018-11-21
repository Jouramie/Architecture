package ca.ulaval.glo4003.domain.user.limit;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.util.TransactionBuilder;
import org.junit.Test;

public class NullLimitTest {

  private static final Transaction SOME_TRANSACTION = new TransactionBuilder().build();

  @Test
  public void whenVerified_thenTransactionNeverExceedLimit() {
    NullLimit limit = new NullLimit();

    boolean result = limit.doesTransactionExceedLimit(SOME_TRANSACTION);

    assertThat(result).isFalse();
  }
}

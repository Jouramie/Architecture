package ca.ulaval.glo4003.infrasctructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.util.TransactionBuilder;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

public class InMemoryTransactionLedgerTest {
  private final Transaction A_TRANSACTION = new TransactionBuilder().withTime(LocalDateTime.now()).build();
  private InMemoryTransactionLedger inMemoryTransactionLedger;

  @Before
  public void setupTransactionLedger() {
    inMemoryTransactionLedger = new InMemoryTransactionLedger();
  }

  @Test
  public void whenSaveTransaction_thenTransactionIsSaved() {
    inMemoryTransactionLedger.save(A_TRANSACTION);

    assertThat(inMemoryTransactionLedger.getTransactions()).contains(A_TRANSACTION);
  }
}

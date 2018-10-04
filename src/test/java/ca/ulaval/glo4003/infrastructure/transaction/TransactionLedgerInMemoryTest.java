package ca.ulaval.glo4003.infrastructure.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.util.TransactionBuilder;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

public class TransactionLedgerInMemoryTest {
  private final Transaction A_TRANSACTION = new TransactionBuilder().withTime(LocalDateTime.now());
  private TransactionLedgerInMemory transactionLedgerInMemory;

  @Before
  public void setupTransactionLedger() {
    transactionLedgerInMemory = new TransactionLedgerInMemory();
  }

  @Test
  public void whenSaveTransaction_thenTransactionIsSaved() {
    transactionLedgerInMemory.save(A_TRANSACTION);

    assertThat(transactionLedgerInMemory.getTransactions()).contains(A_TRANSACTION);
  }
}
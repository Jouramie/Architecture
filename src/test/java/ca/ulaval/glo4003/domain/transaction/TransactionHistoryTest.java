package ca.ulaval.glo4003.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.util.TransactionBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.SortedSet;
import org.junit.Before;
import org.junit.Test;

public class TransactionHistoryTest {

  private TransactionHistory transactionHistory;

  @Before
  public void setupTransactionHistory() {
    transactionHistory = new TransactionHistory();
  }

  @Test
  public void whenSaveTransaction_thenTransactionIsSaved() {
    Transaction aTransaction = new TransactionBuilder().withTime(LocalDateTime.now()).build();

    transactionHistory.save(aTransaction);

    assertThat(transactionHistory.getTransactions(LocalDate.MIN, LocalDate.MAX)).contains(aTransaction);
  }

  @Test
  public void givenTransactions_whenGet_thenReturnTransactionsWithinRange() {
    LocalDate start = LocalDate.of(2018, 11, 3);
    LocalDate end = LocalDate.of(2018, 11, 13);
    Transaction beforeRangeTransaction = new TransactionBuilder().withTime(start.minusDays(1).atStartOfDay()).build();
    Transaction atStartTransaction = new TransactionBuilder().withTime(start.atStartOfDay()).build();
    Transaction inRangeTransaction = new TransactionBuilder().withTime(LocalDate.of(2018, 11, 11).atStartOfDay()).build();
    Transaction atEndTransaction = new TransactionBuilder().withTime(end.atStartOfDay()).build();
    Transaction afterRangeTransaction = new TransactionBuilder().withTime(end.plusDays(1).atStartOfDay()).build();
    transactionHistory.save(beforeRangeTransaction);
    transactionHistory.save(atStartTransaction);
    transactionHistory.save(inRangeTransaction);
    transactionHistory.save(atEndTransaction);
    transactionHistory.save(afterRangeTransaction);

    SortedSet<Transaction> transactions = transactionHistory.getTransactions(start, end);

    assertThat(transactions).containsExactly(atStartTransaction, inRangeTransaction, atEndTransaction);
  }
}

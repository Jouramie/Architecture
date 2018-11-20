package ca.ulaval.glo4003.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.util.TransactionBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeSet;
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
    LocalDateTime datetime = LocalDateTime.now();
    Transaction aTransaction = new TransactionBuilder().withTime(datetime).build();

    transactionHistory.save(aTransaction);

    assertThat(transactionHistory.getTransactions(datetime.toLocalDate())).contains(aTransaction);
  }

  @Test
  public void whenGettingTransactions_thenReturnTransactionsWithinRange() {
    LocalDate date = LocalDate.of(2018, 11, 3);
    Transaction beforeTransaction = new TransactionBuilder().withTime(date.minusDays(1).atStartOfDay()).build();
    Transaction firstTransaction = new TransactionBuilder().withTime(date.atStartOfDay()).build();
    Transaction secondTransaction = new TransactionBuilder().withTime(date.atTime(12, 0, 0)).build();
    Transaction afterTransaction = new TransactionBuilder().withTime(date.plusDays(1).atStartOfDay()).build();
    transactionHistory.save(beforeTransaction);
    transactionHistory.save(firstTransaction);
    transactionHistory.save(secondTransaction);
    transactionHistory.save(afterTransaction);

    TreeSet<Transaction> transactions = transactionHistory.getTransactions(date);

    assertThat(transactions).containsExactly(firstTransaction, secondTransaction);
  }
}

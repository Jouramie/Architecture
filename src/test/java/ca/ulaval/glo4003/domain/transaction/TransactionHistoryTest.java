package ca.ulaval.glo4003.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;

public class TransactionHistoryTest {

  private static final LocalDate SOME_DATE = LocalDate.of(2018, 11, 3);
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
  public void whenGettingTransactions_thenReturnTransactionsAtSpecificDate() {
    Transaction beforeTransaction = new TransactionBuilder().withTime(SOME_DATE.minusDays(1).atStartOfDay()).build();
    Transaction firstTransaction = new TransactionBuilder().withTime(SOME_DATE.atStartOfDay()).build();
    Transaction secondTransaction = new TransactionBuilder().withTime(SOME_DATE.atTime(12, 0, 0)).build();
    Transaction afterTransaction = new TransactionBuilder().withTime(SOME_DATE.plusDays(1).atStartOfDay()).build();
    transactionHistory.save(beforeTransaction);
    transactionHistory.save(firstTransaction);
    transactionHistory.save(secondTransaction);
    transactionHistory.save(afterTransaction);

    TreeSet<Transaction> transactions = transactionHistory.getTransactions(SOME_DATE);

    assertThat(transactions).containsExactly(firstTransaction, secondTransaction);
  }

  @Test
  public void whenGettingTransactions_thenReturnTransactionsWithinRange() {
    LocalDateTime from = SOME_DATE.atStartOfDay();
    LocalDateTime to = SOME_DATE.plusDays(1).atStartOfDay();

    Transaction beforeTransaction = new TransactionBuilder().withTime(SOME_DATE.minusDays(10).atStartOfDay()).build();
    Transaction firstTransaction = new TransactionBuilder().withTime(from).build();
    Transaction secondTransaction = new TransactionBuilder().withTime(to).build();
    Transaction afterTransaction = new TransactionBuilder().withTime(SOME_DATE.plusDays(10).atStartOfDay()).build();
    transactionHistory.save(beforeTransaction);
    transactionHistory.save(firstTransaction);
    transactionHistory.save(secondTransaction);
    transactionHistory.save(afterTransaction);

    List<Transaction> transactions = transactionHistory.getTransactions(from, to);

    assertThat(transactions).containsExactly(firstTransaction, secondTransaction);
  }
}

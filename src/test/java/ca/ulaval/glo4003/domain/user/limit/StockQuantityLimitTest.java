package ca.ulaval.glo4003.domain.user.limit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionBuilder;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import ca.ulaval.glo4003.domain.transaction.TransactionItemBuilder;
import java.time.LocalDateTime;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;

public class StockQuantityLimitTest {
  private static final LocalDateTime SOME_START_DATE = LocalDateTime.of(2018, 4, 1, 9, 0);
  private static final LocalDateTime SOME_INSIDE_DATE = LocalDateTime.of(2018, 6, 1, 0, 0);
  private static final LocalDateTime SOME_END_DATE = LocalDateTime.of(2018, 8, 1, 9, 0);
  private static final LocalDateTime SOME_OUTSIDE_DATE = LocalDateTime.of(2018, 10, 1, 0, 0);

  private static final int MORE_STOCK_QUANTITY = 150;
  private static final int SOME_STOCK_QUANTITY = 100;
  private static final int LESS_STOCK_QUANTITY = 50;

  private Transaction transaction;
  private StockQuantityLimit limit;

  @Before
  public void setup() {
    limit = new StockQuantityLimit(SOME_START_DATE, SOME_END_DATE, SOME_STOCK_QUANTITY);
  }

  @Test
  public void givenStockOverLimit_whenCheckIfTransactionExceed_thenExceptionIsThrown() {
    transaction = generateTransaction(SOME_INSIDE_DATE, MORE_STOCK_QUANTITY);

    ThrowingCallable checkLimit = () -> limit.checkIfTransactionExceed(transaction);

    assertThatThrownBy(checkLimit).isInstanceOf(TransactionLimitExceededExeption.class);
  }

  @Test
  public void givenStockUnderLimit_whenCheckIfTransactionExceed_thenNoExceptionIsThrown()
      throws TransactionLimitExceededExeption {
    transaction = generateTransaction(SOME_INSIDE_DATE, LESS_STOCK_QUANTITY);

    limit.checkIfTransactionExceed(transaction);
  }

  @Test
  public void givenTransactionOutsideLimitTimeSpan_whenCheckIfTransactionExceed_thenNoExceptionIsThrown()
      throws TransactionLimitExceededExeption {
    transaction = generateTransaction(SOME_OUTSIDE_DATE, MORE_STOCK_QUANTITY);

    limit.checkIfTransactionExceed(transaction);
  }

  private Transaction generateTransaction(LocalDateTime date, int quantity) {
    TransactionItem item = new TransactionItemBuilder().withQuantity(quantity).build();
    return new TransactionBuilder().withTime(date).withItem(item).build();
  }
}

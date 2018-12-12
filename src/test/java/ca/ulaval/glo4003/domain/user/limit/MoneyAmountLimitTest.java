package ca.ulaval.glo4003.domain.user.limit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.transaction.Transaction;
import ca.ulaval.glo4003.domain.transaction.TransactionBuilder;
import ca.ulaval.glo4003.domain.transaction.TransactionItem;
import ca.ulaval.glo4003.domain.transaction.TransactionItemBuilder;
import java.time.LocalDateTime;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;

public class MoneyAmountLimitTest {
  private static final LocalDateTime SOME_START_DATE = LocalDateTime.of(2018, 4, 1, 9, 0);
  private static final LocalDateTime SOME_INSIDE_DATE = LocalDateTime.of(2018, 6, 1, 0, 0);
  private static final LocalDateTime SOME_END_DATE = LocalDateTime.of(2018, 8, 1, 9, 0);
  private static final LocalDateTime SOME_OUTSIDE_DATE = LocalDateTime.of(2018, 10, 1, 0, 0);

  private static final MoneyAmount MORE_MONEY = new MoneyAmount(150);
  private static final MoneyAmount SOME_MONEY = new MoneyAmount(100);
  private static final MoneyAmount LESS_MONEY = new MoneyAmount(50);

  private Transaction transaction;
  private MoneyAmountLimit limit;

  @Before
  public void setup() {
    limit = new MoneyAmountLimit(SOME_START_DATE, SOME_END_DATE, SOME_MONEY);
  }

  @Test
  public void givenStockOverLimit_whenEnsureTransactionIsUnderLimit_thenExceptionIsThrown() {
    transaction = generateTransaction(SOME_INSIDE_DATE, MORE_MONEY);

    ThrowingCallable checkLimit = () -> limit.ensureTransactionIsUnderLimit(transaction);

    assertThatThrownBy(checkLimit).isInstanceOf(TransactionLimitExceededExeption.class);
  }

  @Test
  public void givenStockUnderLimit_whenEnsureTransactionIsUnderLimit_thenNoExceptionIsThrown()
      throws TransactionLimitExceededExeption {
    transaction = generateTransaction(SOME_INSIDE_DATE, LESS_MONEY);

    limit.ensureTransactionIsUnderLimit(transaction);
  }

  @Test
  public void givenTransactionOutsideLimitTimeSpan_whenEnsureTransactionIsUnderLimit_thenNoExceptionIsThrown()
      throws TransactionLimitExceededExeption {
    transaction = generateTransaction(SOME_OUTSIDE_DATE, MORE_MONEY);

    limit.ensureTransactionIsUnderLimit(transaction);
  }

  private Transaction generateTransaction(LocalDateTime date, MoneyAmount amount) {
    TransactionItem item = new TransactionItemBuilder().withQuantity(1).withAmount(amount).build();
    return new TransactionBuilder().withTime(date).withItem(item).build();
  }
}

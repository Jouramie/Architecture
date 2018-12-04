package ca.ulaval.glo4003.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionTest {
  private static final TransactionItem AN_ITEM = new TransactionItemBuilder().build();
  private static final TransactionItem ANOTHER_ITEM = new TransactionItemBuilder().build();
  private static final List<TransactionItem> SOME_TRANSACTION_ITEMS
      = Arrays.asList(AN_ITEM, ANOTHER_ITEM);
  private static final TransactionType SOME_TYPE = TransactionType.PURCHASE;
  private static final LocalDateTime SOME_TIME = LocalDateTime.now();
  private static final Transaction formerTransaction = new TransactionBuilder().withTime(LocalDateTime.now()).build();
  private static final Transaction latterTransaction = new TransactionBuilder().withTime(LocalDateTime.now().plusDays(2)).build();
  private static Transaction transaction;
  @Mock
  private Clock someClock;

  @Before
  public void setup() {
    given(someClock.getCurrentTime()).willReturn(SOME_TIME);
    transaction = new Transaction(someClock.getCurrentTime(), SOME_TRANSACTION_ITEMS, SOME_TYPE);
  }

  @Test
  public void whenGettingTotal_thenTotalIsCorrectlyCalculated() {
    MoneyAmount totalAmount = transaction.calculateTotal();

    MoneyAmount expectedTotal = AN_ITEM.amount.add(ANOTHER_ITEM.amount);
    assertThat(totalAmount.toUsd()).isEqualTo(expectedTotal.toUsd());
  }

  @Test
  public void whenCompareToMoreRecentTransaction_thenReturnLessThan0() {
    assertThat(formerTransaction.compareTo(latterTransaction)).isLessThan(0);
  }

  @Test
  public void whenCompareToOlderTransaction_thenReturnGreaterThan0() {
    assertThat(latterTransaction.compareTo(formerTransaction)).isGreaterThan(0);
  }

  @Test
  public void whenCompareToTransactionWithSameDate_thenReturnZero() {
    assertThat(formerTransaction.compareTo(formerTransaction)).isEqualTo(0);
  }
}

package ca.ulaval.glo4003.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.util.TransactionItemBuilder;
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
}

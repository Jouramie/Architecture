package ca.ulaval.glo4003.infrastructure.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.clock.Clock;
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
  private static final List<TransactionItem> SOME_TRANSACTION_ITEMS = Arrays.asList(new TransactionItemBuilder().buildDefault());
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
  public void whenGetTime_thenReturnTransactionsTime() {
    assertThat(transaction.getTime()).isEqualTo(someClock.getCurrentTime());
  }

  @Test
  public void whenGetType_thenReturnTransactionType() {
    assertThat(transaction.getType()).isEqualTo(SOME_TYPE);
  }

  @Test
  public void whenGetAllTransaction_thenReturnTransactions() {
    assertThat(transaction.getListItems()).isEqualTo(SOME_TRANSACTION_ITEMS);
  }
}
package ca.ulaval.glo4003.infrastructure.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionFactoryTest {
  private static final double SOME_LAST_OPEN_VALUE = 40.00;
  private static final Currency SOME_CURRENCY = new Currency("CAD", new BigDecimal(0.77));
  private static final List<TransactionItem> items = Arrays.asList(new TransactionItem("MSFT", 1, new MoneyAmount(SOME_LAST_OPEN_VALUE, SOME_CURRENCY)));
  private static final TransactionType type = TransactionType.PURCHASE;
  private static final LocalDateTime SOME_TIME = LocalDateTime.now();
  private final TransactionFactory factory = new TransactionFactory();

  @Mock
  private Clock someClock;

  @Before
  public void setup() {
    given(someClock.getCurrentTime()).willReturn(SOME_TIME);
  }

  @Test
  public void whenCreateTransaction_thenReturnCreatedTransaction() {
    Transaction expectedTransaction = new Transaction(someClock, items, type);
    Transaction createdTransaction = factory.create(someClock, items, type);
    assertThat(createdTransaction).isEqualToComparingFieldByField(expectedTransaction);
  }
}
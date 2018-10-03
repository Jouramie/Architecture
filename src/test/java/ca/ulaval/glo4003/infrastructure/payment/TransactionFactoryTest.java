package ca.ulaval.glo4003.infrastructure.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.util.TransactionBuilder;
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
public class TransactionFactoryTest {
  private static final List<TransactionItem> items = Arrays.asList(new TransactionItemBuilder().buildDefault());
  private static final TransactionType SOME_TYPE = TransactionType.PURCHASE;
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
    Transaction expectedTransaction = new TransactionBuilder().withType(SOME_TYPE).withItems(items).build(someClock);
    Transaction createdTransaction = factory.create(someClock, items, SOME_TYPE);
    assertThat(createdTransaction).isEqualToComparingFieldByField(expectedTransaction);
  }
}
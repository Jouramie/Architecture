package ca.ulaval.glo4003.service.authentication;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.domain.user.limit.LimitFactory;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class limitServiceTest {
  private final MoneyAmount SOME_MONEY_AMOUNT = new MoneyAmount(20.00, new Currency("CAD", new BigDecimal(0.75)));
  private final ApplicationPeriod SOME_PERIOD = ApplicationPeriod.DAILY;
  private final int SOME_STOCK_QUANTITY = 3;

  @Mock
  Clock clock;
  LimitService service;
  @Mock
  private LimitFactory limitFactory;
  @Mock
  private CurrentUserSession someCurrentUserSession;
  @Mock
  private User someCurrentUser;

  @Before
  public void setUp() {
    given(someCurrentUserSession.getCurrentUser()).willReturn(someCurrentUser);
    service = new LimitService(limitFactory, someCurrentUserSession);
  }

  @Test
  public void whenCreatedAmountMoneyLimit_thenLimitIsCreated() {

    service.createMoneyAmountLimit(SOME_PERIOD, SOME_MONEY_AMOUNT);

    verify(limitFactory).createMoneyAmountLimit(SOME_PERIOD, SOME_MONEY_AMOUNT);
  }

  @Test
  public void whencreateStockQuantityLimit_thenLimitIsCreated() {

    service.createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY);

    verify(limitFactory).createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY);
  }
}

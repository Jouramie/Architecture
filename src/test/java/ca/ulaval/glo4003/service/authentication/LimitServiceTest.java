package ca.ulaval.glo4003.service.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.domain.user.limit.LimitFactory;
import ca.ulaval.glo4003.domain.user.limit.MoneyAmountLimit;
import ca.ulaval.glo4003.domain.user.limit.NullLimit;
import ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit;
import ca.ulaval.glo4003.service.user.limit.LimitService;
import ca.ulaval.glo4003.util.UserBuilder;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LimitServiceTest {

  private static final User SOME_USER = new UserBuilder().build();


  private final BigDecimal SOME_AMOUNT = BigDecimal.valueOf(20.00);
  private final MoneyAmount SOME_MONEY_AMOUNT = new MoneyAmount(SOME_AMOUNT);
  private final ApplicationPeriod SOME_PERIOD = ApplicationPeriod.DAILY;
  private final int SOME_STOCK_QUANTITY = 3;
  private final String SOME_EMAIL = "28gg@email.com";
  //private final MoneyAmountLimitDto moneyAmountLimitDto = new MoneyAmountLimitDto(SOME_MONEY_AMOUNT, , )

  private LimitService service;
  @Mock
  private LimitFactory limitFactory;
  @Mock
  private UserRepository userRepository;
  @Mock
  private User user;
  @Mock
  private MoneyAmountLimit moneyAmountLimit;
  @Mock
  private StockQuantityLimit stockQuantityLimit;

  @Before
  public void setUp() {
    service = new LimitService(limitFactory, userRepository);
  }

  @Test
  public void whenCreateAmountMoneyLimit_thenLimitIsCreated() throws UserNotFoundException {
    given(userRepository.find(SOME_EMAIL)).willReturn(user);
    given(limitFactory.createMoneyAmountLimit(SOME_PERIOD, SOME_MONEY_AMOUNT)).willReturn(moneyAmountLimit);

    service.createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_AMOUNT);

    verify(limitFactory).createMoneyAmountLimit(SOME_PERIOD, SOME_MONEY_AMOUNT);
  }

  @Test
  public void whenCreateStockQuantityLimit_thenLimitIsCreated() throws UserNotFoundException {
    given(userRepository.find(SOME_EMAIL)).willReturn(user);
    given(limitFactory.createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(stockQuantityLimit);

    service.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY);

    verify(limitFactory).createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY);
  }

  @Test
  public void whenCreateAmountMoneyLimit_thenLimitIsAddedToUser() throws UserNotFoundException {
    given(userRepository.find(SOME_EMAIL)).willReturn(SOME_USER);
    given(limitFactory.createMoneyAmountLimit(SOME_PERIOD, SOME_MONEY_AMOUNT)).willReturn(moneyAmountLimit);

    service.createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_AMOUNT);

    assertThat(SOME_USER.getLimit()).isInstanceOf(MoneyAmountLimit.class);
  }

  @Test
  public void whenCreateStockQuantityLimit_thenLimitIsAddedToUser() throws UserNotFoundException {
    given(userRepository.find(SOME_EMAIL)).willReturn(SOME_USER);
    given(limitFactory.createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(stockQuantityLimit);

    service.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY);

    assertThat(SOME_USER.getLimit()).isInstanceOf(StockQuantityLimit.class);
  }

  @Test
  public void whenRemoveLimit_thenLimitIsRemoved() throws UserNotFoundException {
    given(userRepository.find(SOME_EMAIL)).willReturn(SOME_USER);
    given(limitFactory.createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(stockQuantityLimit);

    service.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY);

    service.removeUserLimit(SOME_EMAIL);

    assertThat(SOME_USER.getLimit()).isInstanceOf(NullLimit.class);
  }
}

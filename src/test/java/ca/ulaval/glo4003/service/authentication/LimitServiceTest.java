package ca.ulaval.glo4003.service.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.exceptions.WrongRoleException;
import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.domain.user.limit.LimitFactory;
import ca.ulaval.glo4003.domain.user.limit.MoneyAmountLimit;
import ca.ulaval.glo4003.domain.user.limit.StockQuantityLimit;
import ca.ulaval.glo4003.service.user.limit.LimitAssembler;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.LimitService;
import ca.ulaval.glo4003.service.user.limit.MoneyAmountLimitDto;
import ca.ulaval.glo4003.service.user.limit.StockQuantityLimitDto;
import ca.ulaval.glo4003.util.UserBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LimitServiceTest {

  private static final LocalDateTime SOME_START_DATE = LocalDateTime.of(2018, 4, 1, 9, 0);
  private static final LocalDateTime SOME_END_DATE = LocalDateTime.of(2018, 8, 1, 9, 0);
  private final BigDecimal SOME_AMOUNT = BigDecimal.valueOf(20.00);
  private final MoneyAmount SOME_MONEY_AMOUNT = new MoneyAmount(SOME_AMOUNT);
  private final ApplicationPeriod SOME_PERIOD = ApplicationPeriod.DAILY;
  private final int SOME_STOCK_QUANTITY = 3;
  private final String SOME_EMAIL = "28gg@email.com";
  private final MoneyAmountLimit moneyAmountLimit = new MoneyAmountLimit(SOME_START_DATE, SOME_END_DATE, SOME_MONEY_AMOUNT);
  private final StockQuantityLimit stockQuantityLimit = new StockQuantityLimit(SOME_START_DATE, SOME_END_DATE, SOME_STOCK_QUANTITY);

  @Mock
  private LimitFactory limitFactory;
  @Mock
  private UserRepository userRepository;

  private Investor investor;
  private LimitService service;

  @Before
  public void setUp() {
    service = new LimitService(limitFactory, new LimitAssembler(), userRepository);
    investor = new UserBuilder().withEmail(SOME_EMAIL).buildInvestor();
  }

  @Test
  public void whenCreateAmountMoneyLimit_thenLimitIsCreated() throws UserNotFoundException, WrongRoleException {
    given(userRepository.find(SOME_EMAIL, Investor.class)).willReturn(investor);
    given(limitFactory.createMoneyAmountLimit(SOME_PERIOD, SOME_MONEY_AMOUNT)).willReturn(moneyAmountLimit);

    service.createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_AMOUNT);

    verify(limitFactory).createMoneyAmountLimit(SOME_PERIOD, SOME_MONEY_AMOUNT);
  }

  @Test
  public void whenCreateStockQuantityLimit_thenLimitIsCreated() throws UserNotFoundException, WrongRoleException {
    given(userRepository.find(SOME_EMAIL, Investor.class)).willReturn(investor);
    given(limitFactory.createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(stockQuantityLimit);

    service.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY);

    verify(limitFactory).createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY);
  }

  @Test
  public void whenCreateAmountMoneyLimit_thenLimitIsAddedToUser() throws UserNotFoundException, WrongRoleException {
    given(userRepository.find(SOME_EMAIL, Investor.class)).willReturn(investor);
    given(limitFactory.createMoneyAmountLimit(SOME_PERIOD, SOME_MONEY_AMOUNT)).willReturn(moneyAmountLimit);

    service.createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_AMOUNT);

    assertThat(investor.getLimit()).isInstanceOf(MoneyAmountLimit.class);
  }

  @Test
  public void whenCreateStockQuantityLimit_thenLimitIsAddedToUser() throws UserNotFoundException, WrongRoleException {
    given(userRepository.find(SOME_EMAIL, Investor.class)).willReturn(investor);
    given(limitFactory.createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(stockQuantityLimit);

    service.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY);

    assertThat(investor.getLimit()).isInstanceOf(StockQuantityLimit.class);
  }

  @Test
  public void whenRemoveLimit_thenLimitIsRemoved() throws UserNotFoundException, WrongRoleException {
    given(userRepository.find(SOME_EMAIL, Investor.class)).willReturn(investor);
    given(limitFactory.createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(stockQuantityLimit);
    service.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY);

    service.removeUserLimit(SOME_EMAIL);

    assertThat(investor.getLimit()).isNull();
  }

  @Test
  public void whenCreateStockQuantityLimit_thenLimitDtoIsReturned() throws UserNotFoundException, WrongRoleException {
    given(userRepository.find(SOME_EMAIL, Investor.class)).willReturn(investor);
    given(limitFactory.createStockQuantityLimit(SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(stockQuantityLimit);

    LimitDto limit = service.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY);

    StockQuantityLimitDto stockQuantityLimitDto = new StockQuantityLimitDto(SOME_START_DATE, SOME_END_DATE, SOME_STOCK_QUANTITY);
    assertThat(limit).isEqualToComparingFieldByField(stockQuantityLimitDto);
  }

  @Test
  public void whenCreateAmountMoneyLimit_thenLimitDtoIsReturned() throws UserNotFoundException, WrongRoleException {
    given(userRepository.find(SOME_EMAIL, Investor.class)).willReturn(investor);
    given(limitFactory.createMoneyAmountLimit(SOME_PERIOD, SOME_MONEY_AMOUNT)).willReturn(moneyAmountLimit);

    LimitDto limit = service.createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_MONEY_AMOUNT.getAmount());

    MoneyAmountLimitDto amountLimitDto = new MoneyAmountLimitDto(SOME_START_DATE, SOME_END_DATE, SOME_MONEY_AMOUNT.toUsd());
    assertThat(limit).isEqualToComparingFieldByField(amountLimitDto);
  }
}

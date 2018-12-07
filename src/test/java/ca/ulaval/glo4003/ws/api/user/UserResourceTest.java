package ca.ulaval.glo4003.ws.api.user;

import static ca.ulaval.glo4003.ws.api.validation.InputValidationAsserts.assertThatExceptionContainsErrorFor;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.service.user.UserDto;
import ca.ulaval.glo4003.service.user.UserService;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.LimitService;
import ca.ulaval.glo4003.service.user.limit.MoneyAmountLimitDto;
import ca.ulaval.glo4003.service.user.limit.StockQuantityLimitDto;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiLimitAssembler;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiStockLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.InvestorCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.StockLimitCreationDto;
import ca.ulaval.glo4003.ws.api.validation.InvalidInputException;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

  private static final InvestorCreationDto SOME_CREATION_REQUEST =
      new InvestorCreationDto("email", "password");
  private static final InvestorCreationDto CREATION_REQUEST_WITH_INVALID_EMAIL =
      new InvestorCreationDto("", "password");
  private static final InvestorCreationDto CREATION_REQUEST_WITH_INVALID_PASSWORD =
      new InvestorCreationDto("email", "");

  private static final String SOME_EMAIL = "email";
  private static final UserRole SOME_ROLE = UserRole.INVESTOR;
  private static final LocalDateTime SOME_DATE = LocalDateTime.now();
  private static final int SOME_STOCK_QUANTITY = 12;
  private static final ApplicationPeriod SOME_PERIOD = ApplicationPeriod.DAILY;
  private static final BigDecimal SOME_AMOUNT = BigDecimal.valueOf(20.0);
  private static final StockLimitCreationDto SOME_STOCK_LIMIT_CREATION_DTO = new StockLimitCreationDto(SOME_PERIOD, SOME_STOCK_QUANTITY);
  private static final MoneyAmountLimitCreationDto SOME_MONEY_AMOUNT_LIMIT_CREATION_DTO = new MoneyAmountLimitCreationDto(SOME_PERIOD, SOME_AMOUNT);
  private static final MoneyAmountLimitDto SOME_MONEY_AMOUNT_LIMIT = new MoneyAmountLimitDto(SOME_DATE, SOME_DATE, SOME_AMOUNT);
  private static final StockQuantityLimitDto SOME_STOCK_QUANTITY_LIMIT = new StockQuantityLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
  private static final LimitDto SOME_LIMIT = SOME_STOCK_QUANTITY_LIMIT;
  private static final UserDto SOME_USER_DTO = new UserDto(SOME_EMAIL, SOME_ROLE, SOME_LIMIT);
  @Mock
  private UserService userService;
  @Mock
  private LimitService limitService;

  private UserResource userResource;

  @Before
  public void setup() {
    userResource = new UserResource(userService, limitService, new RequestValidator(),
        new ApiUserAssembler(new ApiLimitAssembler()), new ApiLimitAssembler());
  }

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    given(userService.createInvestorUser(any(), any())).willReturn(SOME_USER_DTO);

    userResource.createInvestor(SOME_CREATION_REQUEST);

    verify(userService).createInvestorUser(SOME_CREATION_REQUEST.email, SOME_CREATION_REQUEST.password);
  }

  @Test
  public void whenCreatingUser_thenReturnConvertedUser() {
    UserDto user = new UserDto(SOME_EMAIL, SOME_ROLE, null);
    given(userService.createInvestorUser(any(), any())).willReturn(user);

    Response response = userResource.createInvestor(SOME_CREATION_REQUEST);

    ApiUserDto expectedUser = new ApiUserDto(SOME_EMAIL, SOME_ROLE, null);
    assertThat(response.getEntity()).isEqualToComparingFieldByField(expectedUser);
  }

  @Test
  public void givenInvalidEmail_whenCreatingUser_thenExceptionIsThrown() {
    ThrowingCallable createUser = () -> userResource.createInvestor(CREATION_REQUEST_WITH_INVALID_EMAIL);

    InvalidInputException exception = catchThrowableOfType(createUser, InvalidInputException.class);
    assertThatExceptionContainsErrorFor(exception, "email");
  }

  @Test
  public void givenInvalidPassword_whenCreatingUser_thenExceptionIsThrown() {
    ThrowingCallable createUser = () -> userResource.createInvestor(CREATION_REQUEST_WITH_INVALID_PASSWORD);

    InvalidInputException exception = Assertions.catchThrowableOfType(createUser, InvalidInputException.class);
    assertThatExceptionContainsErrorFor(exception, "password");
  }

  @Test
  public void whenGetUser_thenGetUserFromService() {
    given(userService.getUser(any())).willReturn(SOME_USER_DTO);

    userResource.getUserByEmail(SOME_EMAIL);

    verify(userService).getUser(SOME_EMAIL);
  }

  @Test
  public void whenGetUser_thenReturnConvertedUser() {
    StockQuantityLimitDto limit = new StockQuantityLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    UserDto user = new UserDto(SOME_EMAIL, SOME_ROLE, limit);
    given(userService.getUser(any())).willReturn(user);

    ApiUserDto resultingUser = userResource.getUserByEmail(SOME_EMAIL);

    ApiStockLimitDto expectedLimit = new ApiStockLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    ApiUserDto expectedUser = new ApiUserDto(SOME_EMAIL, SOME_ROLE, expectedLimit);
    assertThat(resultingUser).isEqualToComparingFieldByFieldRecursively(expectedUser);
  }

  @Test
  public void whenGetUsers_thenGetUsersFromService() {
    given(userService.getUsers()).willReturn(singletonList(SOME_USER_DTO));

    userResource.getUsers();

    verify(userService).getUsers();
  }

  @Test
  public void whenGetUsers_thenReturnConvertedUsers() {
    LimitDto limit = new StockQuantityLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    List<UserDto> users = singletonList(new UserDto(SOME_EMAIL, SOME_ROLE, limit));
    given(userService.getUsers()).willReturn(users);

    List<ApiUserDto> resultingUsers = userResource.getUsers();

    ApiStockLimitDto expectedLimit = new ApiStockLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    List<ApiUserDto> expectedUsers = singletonList(new ApiUserDto(SOME_EMAIL, SOME_ROLE, expectedLimit));
    assertThat(resultingUsers).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(expectedUsers);
  }

  @Test
  public void whenSetUserStockLimit_thenLimitIsCreated() {
    given(limitService.createStockQuantityLimit(any(), any(), anyInt())).willReturn(SOME_STOCK_QUANTITY_LIMIT);

    userResource.setUserStockLimit(SOME_EMAIL, SOME_STOCK_LIMIT_CREATION_DTO);

    verify(limitService).createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY);
  }

  @Test
  public void whenSetUserStockLimit_thenNewLimitIsReturned() {
    StockQuantityLimitDto limit = new StockQuantityLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    given(limitService.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(limit);

    Response resultingLimit = userResource.setUserStockLimit(SOME_EMAIL, SOME_STOCK_LIMIT_CREATION_DTO);

    ApiStockLimitDto expectedStockQuantityLimit = new ApiStockLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
    assertThat(resultingLimit.getEntity()).isEqualToComparingFieldByField(expectedStockQuantityLimit);
  }

  @Test
  public void whenSetUserMoneyAmountLimit_thenLimitIsCreated() {
    given(limitService.createMoneyAmountLimit(any(), any(), any())).willReturn(SOME_MONEY_AMOUNT_LIMIT);

    userResource.setUserMoneyAmountLimit(SOME_EMAIL, SOME_MONEY_AMOUNT_LIMIT_CREATION_DTO);

    verify(limitService).createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_AMOUNT);
  }

  @Test
  public void whenSetUserMoneyAmountLimit_thenNewLimitIsReturned() {
    MoneyAmountLimitDto limit = new MoneyAmountLimitDto(SOME_DATE, SOME_DATE, SOME_AMOUNT);
    given(limitService.createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_AMOUNT)).willReturn(limit);

    Response resultingLimit = userResource.setUserMoneyAmountLimit(SOME_EMAIL, SOME_MONEY_AMOUNT_LIMIT_CREATION_DTO);

    MoneyAmountLimitDto expectedLimit = new MoneyAmountLimitDto(SOME_DATE, SOME_DATE, SOME_AMOUNT);
    assertThat(resultingLimit.getEntity()).isEqualToComparingFieldByField(expectedLimit);
  }

  @Test
  public void whenRemoveUserLimit_thenLimitIsRemoved() {
    userResource.removeUserLimit(SOME_EMAIL);

    verify(limitService).removeUserLimit(SOME_EMAIL);
  }
}

package ca.ulaval.glo4003.ws.api.user;

import static ca.ulaval.glo4003.util.InputValidationTestUtil.assertThatExceptionContainsErrorFor;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import ca.ulaval.glo4003.service.user.UserDto;
import ca.ulaval.glo4003.service.user.UserService;
import ca.ulaval.glo4003.service.user.limit.LimitService;
import ca.ulaval.glo4003.service.user.limit.dto.MoneyAmountLimitDto;
import ca.ulaval.glo4003.service.user.limit.dto.StockLimitDto;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserLimitAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserMoneyAmountLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserStockLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserMoneyAmountLimitCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserStockLimitCreationDto;
import ca.ulaval.glo4003.ws.api.validation.InvalidInputException;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
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

  private static final UserCreationDto SOME_CREATION_REQUEST =
      new UserCreationDto("email", "password");
  private static final UserCreationDto CREATION_REQUEST_WITH_INVALID_EMAIL =
      new UserCreationDto("", "password");
  private static final UserCreationDto CREATION_REQUEST_WITH_INVALID_PASSWORD =
      new UserCreationDto("email", "");

  private static final String SOME_EMAIL = "email";
  private static final UserRole SOME_ROLE = UserRole.INVESTOR;
  private static final UserDto SOME_USER_DTO = new UserDto(SOME_EMAIL, SOME_ROLE);
  private static final ApplicationPeriod SOME_PERIOD = ApplicationPeriod.DAILY;
  private static final int SOME_STOCK_QUANTITY = 10;
  private static final double SOME_AMOUNT = 20.0;
  private static final UserStockLimitCreationDto SOME_USER_STOCK_LIMIT_DTO = new UserStockLimitCreationDto(SOME_PERIOD, SOME_STOCK_QUANTITY);
  private static final UserMoneyAmountLimitCreationDto SOME_USER_AMOUNT_LIMIT_DTO = new UserMoneyAmountLimitCreationDto(SOME_PERIOD, SOME_AMOUNT);
  @Mock
  private UserService userService;
  @Mock
  private LimitService limitService;
  @Mock
  private StockLimitDto stockLimitDto;
  @Mock
  private MoneyAmountLimitDto moneyAmountLimitDto;
  @Mock
  private ApiUserLimitAssembler apiUserLimitAssembler;
  @Mock
  private ApiUserStockLimitDto expectedApiUserStockLimitDto;
  @Mock
  private ApiUserMoneyAmountLimitDto expectedApiAmountLimitDto;

  private UserResourceImpl userResource;

  @Before
  public void setup() {
    userResource = new UserResourceImpl(userService, limitService, new RequestValidator(), new ApiUserAssembler(), new ApiUserLimitAssembler());
  }

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    given(userService.createInvestorUser(any(), any())).willReturn(SOME_USER_DTO);

    userResource.createUser(SOME_CREATION_REQUEST);

    verify(userService).createInvestorUser(SOME_CREATION_REQUEST.email, SOME_CREATION_REQUEST.password);
  }

  @Test
  public void whenCreatingUser_thenReturnConvertedUser() {
    UserDto user = new UserDto(SOME_EMAIL, SOME_ROLE);
    given(userService.createInvestorUser(any(), any())).willReturn(user);

    Response response = userResource.createUser(SOME_CREATION_REQUEST);

    ApiUserDto expectedUser = new ApiUserDto(SOME_EMAIL, SOME_ROLE, null);
    assertThat(response.getEntity()).isEqualToComparingFieldByField(expectedUser);
  }

  @Test
  public void givenInvalidEmail_whenCreatingUser_thenExceptionIsThrown() {
    ThrowingCallable createUser = () -> userResource.createUser(CREATION_REQUEST_WITH_INVALID_EMAIL);

    InvalidInputException exception = catchThrowableOfType(createUser, InvalidInputException.class);
    assertThatExceptionContainsErrorFor(exception, "email");
  }

  @Test
  public void givenInvalidPassword_whenCreatingUser_thenExceptionIsThrown() {
    ThrowingCallable createUser = () -> userResource.createUser(CREATION_REQUEST_WITH_INVALID_PASSWORD);

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
    UserDto user = new UserDto(SOME_EMAIL, SOME_ROLE);
    given(userService.getUser(any())).willReturn(user);

    ApiUserDto resultingUser = userResource.getUserByEmail(SOME_EMAIL);

    ApiUserDto expectedUser = new ApiUserDto(SOME_EMAIL, SOME_ROLE, null);
    assertThat(resultingUser).isEqualToComparingFieldByField(expectedUser);
  }

  @Test
  public void whenGetUsers_thenGetUsersFromService() {
    given(userService.getUsers()).willReturn(singletonList(SOME_USER_DTO));

    userResource.getUsers();

    verify(userService).getUsers();
  }

  @Test
  public void whenGetUsers_thenReturnConvertedUsers() {
    List<UserDto> users = singletonList(new UserDto(SOME_EMAIL, SOME_ROLE));
    given(userService.getUsers()).willReturn(users);

    List<ApiUserDto> resultingUsers = userResource.getUsers();

    List<ApiUserDto> expectedUsers = singletonList(new ApiUserDto(SOME_EMAIL, SOME_ROLE, null));
    assertThat(resultingUsers).usingFieldByFieldElementComparator().containsExactlyElementsOf(expectedUsers);
  }

  @Test
  public void whenSetUserStockLimit_thenLimitIsCreated() {
    given(limitService.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(stockLimitDto);
    Response userLimitDto = userResource.setUserStockLimit(SOME_EMAIL, SOME_USER_STOCK_LIMIT_DTO);

    verify(limitService).createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY);
  }

  @Test
  public void whenSetUserStockLimit_thenNewLimitIsReturned() {
    given(limitService.createStockQuantityLimit(SOME_EMAIL, SOME_PERIOD, SOME_STOCK_QUANTITY)).willReturn(stockLimitDto);
    given(apiUserLimitAssembler.toDtoStockLimit(stockLimitDto)).willReturn(expectedApiUserStockLimitDto);

    Response results = userResource.setUserStockLimit(SOME_EMAIL, SOME_USER_STOCK_LIMIT_DTO);

    assertThat(results.getEntity()).isEqualToComparingFieldByField(expectedApiUserStockLimitDto);
  }

  @Test
  public void whenSetUserMoneyAmountLimit_thenLimitIsCreated() {
    given(limitService.createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_AMOUNT)).willReturn(moneyAmountLimitDto);

    Response userLimitDto = userResource.setUserMoneyAmountLimit(SOME_EMAIL, SOME_USER_AMOUNT_LIMIT_DTO);

    verify(limitService).createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_AMOUNT);
  }

  @Test
  public void whenSetUserMoneyAmountLimit_thenNewLimitIsReturned() {
    given(limitService.createMoneyAmountLimit(SOME_EMAIL, SOME_PERIOD, SOME_AMOUNT)).willReturn(moneyAmountLimitDto);
    given(apiUserLimitAssembler.toDtoMoneyAmountLimit(moneyAmountLimitDto)).willReturn(expectedApiAmountLimitDto);

    Response results = userResource.setUserMoneyAmountLimit(SOME_EMAIL, SOME_USER_AMOUNT_LIMIT_DTO);

    assertThat(results.getEntity()).isEqualToComparingFieldByField(expectedApiAmountLimitDto);
  }

  @Test
  public void whenRemoveUserLimit_thenLimitIsRemoved() {

    Response response = userResource.removeUserLimit(SOME_EMAIL);

    verify(limitService).removeUserLimit(SOME_EMAIL);
  }
}

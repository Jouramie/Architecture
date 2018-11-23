package ca.ulaval.glo4003.ws.api.user;

import static ca.ulaval.glo4003.util.InputValidationTestUtil.assertThatExceptionContainsErrorFor;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.user.UserDto;
import ca.ulaval.glo4003.service.user.UserService;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import ca.ulaval.glo4003.service.user.limit.StockQuantityLimitDto;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiLimitAssembler;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiStockLimitDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.InvestorCreationDto;
import ca.ulaval.glo4003.ws.api.validation.InvalidInputException;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
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
  private static final LimitDto SOME_LIMIT = new StockQuantityLimitDto(SOME_DATE, SOME_DATE, SOME_STOCK_QUANTITY);
  private static final UserDto SOME_USER_DTO = new UserDto(SOME_EMAIL, SOME_ROLE, SOME_LIMIT);

  @Mock
  private UserService userService;

  private UserResourceImpl userResource;

  @Before
  public void setup() {
    userResource = new UserResourceImpl(userService, new RequestValidator(),
        new ApiUserAssembler(new ApiLimitAssembler()));
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
}

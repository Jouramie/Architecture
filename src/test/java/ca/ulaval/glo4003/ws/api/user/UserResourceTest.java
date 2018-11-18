package ca.ulaval.glo4003.ws.api.user;

import static ca.ulaval.glo4003.util.InputValidationTestUtil.assertThatExceptionContainsErrorFor;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.authentication.UserDto;
import ca.ulaval.glo4003.service.authentication.UserService;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
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

  @Mock
  private UserService userService;

  private UserResourceImpl userResource;

  @Before
  public void setup() {
    userResource = new UserResourceImpl(userService, new RequestValidator(), new ApiUserAssembler());
  }

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    given(userService.createInvestorUser(any(), any())).willReturn(mock(UserDto.class));

    userResource.createUser(SOME_CREATION_REQUEST);

    verify(userService).createInvestorUser(SOME_CREATION_REQUEST.email, SOME_CREATION_REQUEST.password);
  }

  @Test
  public void whenCreatingUser_thenReturnConvertedUser() {
    UserDto someUser = new UserDto(SOME_EMAIL, SOME_ROLE);
    given(userService.createInvestorUser(any(), any())).willReturn(someUser);

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
  public void givenInvalidPassword_whenCreatingUser_thenInvalidInputExceptionShouldBeThrown() {
    ThrowingCallable createUser = () -> userResource.createUser(CREATION_REQUEST_WITH_INVALID_PASSWORD);

    InvalidInputException exception = Assertions.catchThrowableOfType(createUser, InvalidInputException.class);
    assertThatExceptionContainsErrorFor(exception, "password");
  }

  @Test
  public void whenGetUser_thenGetUserFromService() {
    given(userService.getUser(any())).willReturn(mock(UserDto.class));

    userResource.getUserByEmail(SOME_EMAIL);

    verify(userService).getUser(SOME_EMAIL);
  }

  @Test
  public void whenGetUser_thenReturnConvertedUser() {
    UserDto someUser = new UserDto(SOME_EMAIL, SOME_ROLE);
    given(userService.getUser(any())).willReturn(someUser);

    ApiUserDto resultingUser = userResource.getUserByEmail(SOME_EMAIL);

    ApiUserDto expectedUser = new ApiUserDto(SOME_EMAIL, SOME_ROLE, null);
    assertThat(resultingUser).isEqualToComparingFieldByField(expectedUser);
  }

  @Test
  public void whenGetUsers_thenGetUsersFromService() {
    given(userService.getUsers()).willReturn(singletonList(mock(UserDto.class)));

    userResource.getUsers();

    verify(userService).getUsers();
  }

  @Test
  public void whenGetUsers_thenReturnConvertedUsers() {
    List<UserDto> someUsers = singletonList(new UserDto(SOME_EMAIL, SOME_ROLE));
    given(userService.getUsers()).willReturn(someUsers);

    List<ApiUserDto> resultingUsers = userResource.getUsers();

    List<ApiUserDto> expectedUsers = singletonList(new ApiUserDto(SOME_EMAIL, SOME_ROLE, null));
    assertThat(resultingUsers).usingFieldByFieldElementComparator().containsExactlyElementsOf(expectedUsers);
  }
}

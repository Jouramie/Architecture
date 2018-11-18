package ca.ulaval.glo4003.ws.api.user;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.authentication.UserDto;
import ca.ulaval.glo4003.service.authentication.UserService;
import ca.ulaval.glo4003.ws.api.user.assemblers.ApiUserAssembler;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import ca.ulaval.glo4003.ws.api.validation.InvalidInputException;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import java.util.List;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
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

  private static final String ERROR_MESSAGE_PATTERN = "%s.+";
  private static final String SOME_EMAIL = "email";

  @Mock
  private UserService userService;
  @Mock
  private ApiUserAssembler apiUserAssembler;

  private UserResourceImpl userResource;

  @Before
  public void setup() {
    userResource = new UserResourceImpl(userService, new RequestValidator(), apiUserAssembler);
  }

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    userResource.createUser(SOME_CREATION_REQUEST);

    verify(userService).createInvestorUser(SOME_CREATION_REQUEST.email, SOME_CREATION_REQUEST.password);
  }

  @Test
  public void whenCreatingUser_thenUserIsConvertedToApiDto() {
    UserDto user = mock(UserDto.class);
    given(userService.createInvestorUser(any(), any())).willReturn(user);

    userResource.createUser(SOME_CREATION_REQUEST);

    verify(apiUserAssembler).toDto(user);
  }

  @Test
  public void whenCreatingUser_thenUserIsReturned() {
    ApiUserDto expectedUser = mock(ApiUserDto.class);
    given(apiUserAssembler.toDto(any(UserDto.class))).willReturn(expectedUser);

    Response response = userResource.createUser(SOME_CREATION_REQUEST);

    assertThat(response.getEntity()).isEqualTo(expectedUser);
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
    userResource.getUserByEmail(SOME_EMAIL);

    verify(userService).getUser(SOME_EMAIL);
  }

  @Test
  public void whenGetUser_thenConvertUserToApiDto() {
    UserDto user = mock(UserDto.class);
    given(userService.getUser(any())).willReturn(user);

    userResource.getUserByEmail(SOME_EMAIL);

    verify(apiUserAssembler).toDto(user);
  }

  @Test
  public void whenGetUser_thenReturnConvertedUser() {
    ApiUserDto expectedUser = mock(ApiUserDto.class);
    given(apiUserAssembler.toDto(any(UserDto.class))).willReturn(expectedUser);

    ApiUserDto resultingUser = userResource.getUserByEmail(SOME_EMAIL);

    assertThat(resultingUser).isEqualTo(expectedUser);
  }

  @Test
  public void whenGetUsers_thenGetUsersFromService() {
    userResource.getUsers();

    verify(userService).getUsers();
  }

  @Test
  public void whenGetUsers_thenConvertUsersToApiDto() {
    List<UserDto> users = singletonList(mock(UserDto.class));
    given(userService.getUsers()).willReturn(users);

    userResource.getUsers();

    verify(apiUserAssembler).toDto(users);
  }

  @Test
  public void whenGetUsers_thenReturnConvertedUsers() {
    List<ApiUserDto> expectedUsers = singletonList(mock(ApiUserDto.class));
    given(apiUserAssembler.toDto(Matchers.<List<UserDto>>any())).willReturn(expectedUsers);

    List<ApiUserDto> resultingUsers = userResource.getUsers();

    assertThat(resultingUsers).isEqualTo(expectedUsers);
  }

  private void assertThatExceptionContainsErrorFor(InvalidInputException exception, String field) {
    String expectMessageErrorPattern = String.format(ERROR_MESSAGE_PATTERN, field);
    assertThat(exception.getInputErrors().inputErrors)
        .anyMatch(errorMessage -> Pattern.matches(expectMessageErrorPattern, errorMessage));
  }
}

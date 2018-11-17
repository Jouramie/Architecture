package ca.ulaval.glo4003.ws.api.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;
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
    UserDto userDto = mock(UserDto.class);
    given(userService.createInvestorUser(any(), any())).willReturn(userDto);

    userResource.createUser(SOME_CREATION_REQUEST);

    verify(apiUserAssembler).toDto(userDto);
  }

  @Test
  public void whenCreatingUser_thenUserIsReturned() {
    ApiUserDto apiUserDto = mock(ApiUserDto.class);
    given(apiUserAssembler.toDto(any())).willReturn(apiUserDto);

    Response response = userResource.createUser(SOME_CREATION_REQUEST);

    assertThat(response.getEntity()).isEqualTo(apiUserDto);
  }

  @Test
  public void givenInvalidEmail_whenCreatingUser_thenInvalidInputExceptionShouldBeThrown() {
    Throwable thrown =
        catchThrowable(() -> userResource.createUser(CREATION_REQUEST_WITH_INVALID_EMAIL));

    assertThat(thrown).isInstanceOf(InvalidInputException.class);
    InvalidInputException exception = (InvalidInputException) thrown;
    assertThatExceptionContainsErrorFor(exception, "email");
  }

  @Test
  public void givenInvalidPassword_whenCreatingUser_thenInvalidInputExceptionShouldBeThrown() {
    Throwable thrown =
        catchThrowable(() -> userResource.createUser(CREATION_REQUEST_WITH_INVALID_PASSWORD));

    assertThat(thrown).isInstanceOf(InvalidInputException.class);
    InvalidInputException exception = (InvalidInputException) thrown;
    assertThatExceptionContainsErrorFor(exception, "password");
  }

  @Test
  public void givenSomeEmail_whenGetUser_thenGetUserFromService() {
    userResource.getUserByEmail(SOME_EMAIL);

    verify(userService).getUser(SOME_EMAIL);
  }

  @Test
  public void givenSomeEmail_whenGetUser_thenConvertUserToApiDto() {
    UserDto userDto = mock(UserDto.class);
    given(userService.getUser(any())).willReturn(userDto);

    userResource.getUserByEmail(SOME_EMAIL);

    verify(apiUserAssembler).toDto(userDto);
  }

  @Test
  public void givenSomeEmail_whenGetUser_thenReturnConvertedUser() {
    ApiUserDto apiUserDto = mock(ApiUserDto.class);
    given(apiUserAssembler.toDto(any())).willReturn(apiUserDto);

    ApiUserDto returnedApiUserDto = userResource.getUserByEmail(SOME_EMAIL);

    assertThat(returnedApiUserDto).isEqualTo(apiUserDto);
  }

  private void assertThatExceptionContainsErrorFor(InvalidInputException exception, String field) {
    String expectMessageErrorPattern = String.format(ERROR_MESSAGE_PATTERN, field);
    assertThat(exception.getInputErrors().inputErrors)
        .anyMatch(errorMessage -> Pattern.matches(expectMessageErrorPattern, errorMessage));
  }
}

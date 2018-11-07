package ca.ulaval.glo4003.ws.api.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.authentication.UserCreationService;
import ca.ulaval.glo4003.service.authentication.UserDto;
import ca.ulaval.glo4003.ws.api.user.UserResourceImpl;
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

  private static final UserCreationDto CREATION_REQUEST_WITHOUT_ROLE =
      new UserCreationDto("email", "passord");

  private static final String ERROR_MESSAGE_PATTERN = "%s.+";
  private static final UserDto SOME_USER_RESPONSE = new UserDto("email", UserRole.INVESTOR);
  private static final ApiUserDto SOME_API_USER_RESPONSE = new ApiUserDto("email", UserRole.INVESTOR, null);

  @Mock
  private UserCreationService userCreationService;

  private RequestValidator requestValidator;

  @Mock
  private ApiUserAssembler apiUserAssembler;

  private UserResourceImpl userResource;

  @Before
  public void setup() {
    requestValidator = new RequestValidator();
    userResource = new UserResourceImpl(userCreationService, requestValidator, apiUserAssembler);
  }

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    userResource.createUser(SOME_CREATION_REQUEST);

    verify(userCreationService).createInvestorUser(SOME_CREATION_REQUEST.email, SOME_CREATION_REQUEST.password);
  }

  @Test
  public void whenCreatingUser_thenReturned() {
    given(userCreationService.createInvestorUser(SOME_CREATION_REQUEST.email, SOME_CREATION_REQUEST.password)).willReturn(SOME_USER_RESPONSE);
    given(apiUserAssembler.toDto(SOME_USER_RESPONSE)).willReturn(SOME_API_USER_RESPONSE);

    Response response = userResource.createUser(SOME_CREATION_REQUEST);
    assertThat(response.getEntity()).isEqualTo(SOME_API_USER_RESPONSE);
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

  private void assertThatExceptionContainsErrorFor(InvalidInputException exception, String field) {
    String expectMessageErrorPattern = String.format(ERROR_MESSAGE_PATTERN, field);
    assertThat(exception.getInputErrors().inputErrors)
        .anyMatch(errorMessage -> Pattern.matches(expectMessageErrorPattern, errorMessage));
  }
}

package ca.ulaval.glo4003.ws.api.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.api.validation.InvalidInputException;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import ca.ulaval.glo4003.ws.application.user.UserCreationService;
import ca.ulaval.glo4003.ws.domain.user.UserRole;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceImplTest {

  private static final UserCreationDto CREATION_REQUEST_WITH_INVALID_USERNAME =
      new UserCreationDto("", "password", UserRole.ADMINISTRATOR);

  private static final UserCreationDto CREATION_REQUEST_WITH_INVALID_PASSWORD =
      new UserCreationDto("username", "", UserRole.ADMINISTRATOR);

  private static final UserCreationDto CREATION_REQUEST_WITHOUT_ROLE =
      new UserCreationDto("username", "passord", null);

  private static final UserCreationDto CREATION_REQUEST =
      new UserCreationDto("username", "password", UserRole.ADMINISTRATOR);

  private static final String ERROR_MESSAGE_PATTERN = "%s.+";

  @Mock
  private UserCreationService userCreationService;

  private RequestValidator requestValidator;

  private UserResourceImpl userResource;

  @Before
  public void setup() {
    requestValidator = new RequestValidator();
    userResource = new UserResourceImpl(userCreationService, requestValidator);
  }

  @Test
  public void whenCreatingUser_thenUserIsCreated() {
    userResource.createUser(CREATION_REQUEST);

    verify(userCreationService).createUser(CREATION_REQUEST);
  }

  @Test
  public void givenInvalidUserName_whenCreatingUser_thenInvalidInputExceptionShouldBeThrown() {
    Throwable thrown =
        catchThrowable(() -> userResource.createUser(CREATION_REQUEST_WITH_INVALID_USERNAME));

    assertThat(thrown).isInstanceOf(InvalidInputException.class);
    InvalidInputException exception = (InvalidInputException) thrown;
    assertThatExceptionContainsErrorFor(exception, "username");
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
  public void givenNullRole_whenCreatingUser_thenInvalidInputExceptionShouldBeThrown() {
    Throwable thrown = catchThrowable(() -> userResource.createUser(CREATION_REQUEST_WITHOUT_ROLE));

    assertThat(thrown).isInstanceOf(InvalidInputException.class);
    InvalidInputException exception = (InvalidInputException) thrown;
    assertThatExceptionContainsErrorFor(exception, "role");
  }

  private void assertThatExceptionContainsErrorFor(InvalidInputException exception, String field) {
    String expectMessageErrorPattern = String.format(ERROR_MESSAGE_PATTERN, field);
    assertThat(exception.getInputErrors().inputErrors)
        .anyMatch(errorMessage -> Pattern.matches(expectMessageErrorPattern, errorMessage));
  }
}
package ca.ulaval.glo4003.ws.api.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.authentication.UserCreationService;
import ca.ulaval.glo4003.ws.api.validation.InvalidInputException;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceImplTest {

  private static final UserCreationDto SOME_CREATION_REQUEST =
      new UserCreationDto("email", "password");
  private static final UserCreationDto CREATION_REQUEST_WITH_INVALID_EMAIL =
      new UserCreationDto("", "password");

  private static final UserCreationDto CREATION_REQUEST_WITH_INVALID_PASSWORD =
      new UserCreationDto("email", "");

  private static final UserCreationDto CREATION_REQUEST_WITHOUT_ROLE =
      new UserCreationDto("email", "passord");

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
    userResource.createUser(SOME_CREATION_REQUEST);

    verify(userCreationService).createInvestorUser(SOME_CREATION_REQUEST);
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

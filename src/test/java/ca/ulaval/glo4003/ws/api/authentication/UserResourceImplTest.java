package ca.ulaval.glo4003.ws.api.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.ws.api.InvalidInputException;
import ca.ulaval.glo4003.ws.application.user.UserCreationService;
import ca.ulaval.glo4003.ws.domain.user.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
  @Mock
  private UserCreationService userCreationService;

  @InjectMocks
  private UserResourceImpl userResource;

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
    assertThat(exception.getInputErrors().inputErrors).contains("username must not be blank");
  }

  @Test
  public void givenInvalidPassword_whenCreatingUser_thenInvalidInputExceptionShouldBeThrown() {
    Throwable thrown =
        catchThrowable(() -> userResource.createUser(CREATION_REQUEST_WITH_INVALID_PASSWORD));

    assertThat(thrown).isInstanceOf(InvalidInputException.class);
    InvalidInputException exception = (InvalidInputException) thrown;
    assertThat(exception.getInputErrors().inputErrors)
        .contains("password size must be between 1 and " + Integer.MAX_VALUE);
  }

  @Test
  public void givenNullRole_whenCreatingUser_thenInvalidInputExceptionShouldBeThrown() {
    Throwable thrown = catchThrowable(() -> userResource.createUser(CREATION_REQUEST_WITHOUT_ROLE));

    assertThat(thrown).isInstanceOf(InvalidInputException.class);
    InvalidInputException exception = (InvalidInputException) thrown;
    assertThat(exception.getInputErrors().inputErrors).contains("role must not be null");
  }
}
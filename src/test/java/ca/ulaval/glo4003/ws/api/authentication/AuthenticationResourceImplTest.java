package ca.ulaval.glo4003.ws.api.authentication;


import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.authentication.AuthenticationService;
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
public class AuthenticationResourceImplTest {

  private static final AuthenticationRequestDto SOME_AUTHENTICATION_REQUEST =
      new AuthenticationRequestDto("email", "password");

  private static final AuthenticationResponseDto SOME_AUTHENTICATION_RESPONSE
      = new AuthenticationResponseDto("TOKEN");

  private static final AuthenticationRequestDto AUTHENTICATION_REQUEST_WITHOUT_EMAIL =
      new AuthenticationRequestDto(null, "password");

  private static final AuthenticationRequestDto AUTHENTICATION_REQUEST_WITHOUT_PASSWORD =
      new AuthenticationRequestDto("email", null);

  private static final String ERROR_MESSAGE_PATTERN = "%s.+";

  @Mock
  private AuthenticationService authenticationService;

  private RequestValidator requestValidator;

  private AuthenticationResourceImpl authenticationResource;

  @Before
  public void setup() {
    requestValidator = new RequestValidator();
    authenticationResource
        = new AuthenticationResourceImpl(authenticationService, requestValidator);
  }

  @Test
  public void whenAuthenticatingUser_thenUserIsAuthenticated() {
    authenticationResource.authenticate(SOME_AUTHENTICATION_REQUEST);

    verify(authenticationService).authenticate(SOME_AUTHENTICATION_REQUEST);
  }

  @Test
  public void givenValidAuthenticationRequest_whenAuthenticatingUser_thenResponseStatusIsAccepted() {
    Response response = authenticationResource.authenticate(SOME_AUTHENTICATION_REQUEST);

    assertThat(response.getStatus()).isEqualTo(ACCEPTED.getStatusCode());
  }

  @Test
  public void givenValidAuthenticationRequest_whenAuthenticatingUser_thenTokenIsReturned() {
    given(authenticationService.authenticate(SOME_AUTHENTICATION_REQUEST))
        .willReturn(SOME_AUTHENTICATION_RESPONSE);

    Response response = authenticationResource.authenticate(SOME_AUTHENTICATION_REQUEST);

    assertThat(response.getEntity()).isEqualTo(SOME_AUTHENTICATION_RESPONSE);
  }

  @Test
  public void givenNullEmail_whenAuthenticatingUser_thenInvalidInputExceptionShouldBeThrown() {
    Throwable thrown = catchThrowable(
        () -> authenticationResource.authenticate(AUTHENTICATION_REQUEST_WITHOUT_EMAIL));

    assertThat(thrown).isInstanceOf(InvalidInputException.class);
    InvalidInputException exception = (InvalidInputException) thrown;
    assertThatExceptionContainsErrorFor(exception, "email");
  }

  @Test
  public void givenNullPassword_whenAuthenticatingUser_thenInvalidInputExceptionShouldBeThrown() {
    Throwable thrown = catchThrowable(
        () -> authenticationResource.authenticate(AUTHENTICATION_REQUEST_WITHOUT_PASSWORD));

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

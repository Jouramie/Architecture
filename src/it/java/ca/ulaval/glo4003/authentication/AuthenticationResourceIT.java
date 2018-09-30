package ca.ulaval.glo4003.authentication;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.any;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import ca.ulaval.glo4003.ws.domain.user.UserRole;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class AuthenticationResourceIT {


  private static final String SOME_USERNAME = "username";

  private static final String SOME_PASSWORD = "password";

  private static final UserCreationDto A_CREATION_REQUEST =
      new UserCreationDto(SOME_USERNAME, SOME_PASSWORD, UserRole.ADMINISTRATOR);

  private static final AuthenticationRequestDto AN_AUTHENTICATION_REQUEST =
      new AuthenticationRequestDto(SOME_USERNAME, SOME_PASSWORD);

  private static final AuthenticationRequestDto WRONG_PASSWORD_AUTHENTICATION_REQUEST =
      new AuthenticationRequestDto(SOME_USERNAME, SOME_PASSWORD + "wrong");

  private static final AuthenticationRequestDto AN_INVALID_AUTHENTICATION_REQUEST
      = new AuthenticationRequestDto(null, null);

  private static final String USERS_ROUTE = "/api/users";

  private static final String AUTHENTICATION_ROUTE = "/api/authenticate";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void givenUserInformation_whenAuthenticating_thenReturnAuthenticationToken() {
    givenUserAlreadyRegistered();
    //@formatter:off
    given()
        .body(AN_AUTHENTICATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(AUTHENTICATION_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("token", any(String.class));
    //@formatter:on
  }

  @Test
  public void givenIncorrectUserInformation_whenAuthenticating_thenReturnBadRequest() {
    givenUserAlreadyRegistered();
    //@formatter:off
    given()
        .body(WRONG_PASSWORD_AUTHENTICATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(AUTHENTICATION_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvalidInputs_whenAuthenticatingUser_thenBadRequest() {
    //@formatter:off
    given()
        .body(AN_INVALID_AUTHENTICATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(AUTHENTICATION_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode())
        .body("inputErrors", any(List.class));
    //@formatter:on
  }

  private void givenUserAlreadyRegistered() {
    given().body(A_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON).post(USERS_ROUTE);
  }
}

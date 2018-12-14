package ca.ulaval.glo4003.it.authentication;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.any;

import ca.ulaval.glo4003.it.ResetServerBetweenTest;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.user.dto.InvestorCreationDto;
import io.restassured.http.Header;
import io.restassured.response.Response;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class AuthenticationIT {

  private static final String SOME_EMAIL = "email";

  private static final String SOME_PASSWORD = "password";

  private static final InvestorCreationDto A_CREATION_REQUEST =
      new InvestorCreationDto(SOME_EMAIL, SOME_PASSWORD);

  private static final ApiAuthenticationRequestDto AN_AUTHENTICATION_REQUEST =
      new ApiAuthenticationRequestDto(SOME_EMAIL, SOME_PASSWORD);

  private static final ApiAuthenticationRequestDto WRONG_PASSWORD_AUTHENTICATION_REQUEST =
      new ApiAuthenticationRequestDto(SOME_EMAIL, SOME_PASSWORD + "wrong");

  private static final ApiAuthenticationRequestDto WRONG_USER_AUTHENTICATION_REQUEST =
      new ApiAuthenticationRequestDto("zero janvier", SOME_PASSWORD);

  private static final ApiAuthenticationRequestDto AN_INVALID_AUTHENTICATION_REQUEST
      = new ApiAuthenticationRequestDto(null, null);

  private static final String USERS_ROUTE = "/api/users";

  private static final String AUTHENTICATION_ROUTE = "/api/authenticate";
  private static final String LOGOUT_ROUTE = "/api/logout";

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
        .statusCode(ACCEPTED.getStatusCode())
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
  public void givenNonExistingUser_whenAuthenticating_thenReturnBadRequest() {
    givenUserAlreadyRegistered();
    //@formatter:off
    given()
        .body(WRONG_USER_AUTHENTICATION_REQUEST)
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
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }


  @Test
  public void givenNotAuthenticatedUser_whenLoggingOut_thenResponseIsUnauthorized() {
    //@formatter:off
    when()
        .post(LOGOUT_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenUserAuthenticated_whenLoggingOut_ThenOk() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .post(LOGOUT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvalidToken_whenLoggingOut_ThenUnauthorized() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token + "wrong");

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .post(LOGOUT_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  private void givenUserAlreadyRegistered() {
    given().body(A_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON).post(USERS_ROUTE);
  }

  private String givenUserAlreadyAuthenticated() {
    Response response = given().body(AN_AUTHENTICATION_REQUEST).contentType(MediaType.APPLICATION_JSON)
        .when().post(AUTHENTICATION_ROUTE);

    return response.jsonPath().getString("token");
  }
}

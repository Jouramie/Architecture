package ca.ulaval.glo4003.authentication;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import io.restassured.http.Header;
import io.restassured.response.Response;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class TokenRevocationIT {

  private static final String REVOCATION_ROUTE = "/api/logout";

  private static final String USERS_ROUTE = "/api/users";

  private static final String AUTHENTICATION_ROUTE = "/api/authenticate";

  private static final String SOME_EMAIL = "a email";

  private static final String SOME_PASSWORD = "a password";

  private static final UserCreationDto A_CREATION_REQUEST =
      new UserCreationDto(SOME_EMAIL, SOME_PASSWORD);

  private static final AuthenticationRequestDto AN_AUTHENTICATION_REQUEST =
      new AuthenticationRequestDto(SOME_EMAIL, SOME_PASSWORD);

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void givenNotAuthenticatedUser_whenRevokingToken_thenResponseIsUnauthorized() {
    //@formatter:off
    given()
    .when()
        .post(REVOCATION_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenUserAuthenticated_whenRevokingToken_ThenOk() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .post(REVOCATION_ROUTE)
    .then()
        .statusCode(OK.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvalidToken_whenRevokingToken_ThenOk() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token + "wrong");

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .post(REVOCATION_ROUTE)
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

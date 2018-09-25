package ca.ulaval.glo4003.authentication;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.any;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResponseDto;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class AuthenticationResourceIT {

  private static final AuthenticationRequestDto AN_AUTHENTICATION_REQUEST =
      new AuthenticationRequestDto("user", "password");

  private static final String AUTHENTICATION_ROUTE = "/api/authenticate";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void givenUserInformation_whenAuthenticating_thenReturnAuthenticationToken() {
    //@formatter:off
    given()
        .body(AN_AUTHENTICATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(AUTHENTICATION_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body(any(AuthenticationResponseDto.class));
    //@formatter:on
  }

  @Test
  public void givenIncorrectUserInformation_whenAuthenticating_thenReturnBadRequest() {
    //@formatter:off
    given()
        .body(AN_AUTHENTICATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(AUTHENTICATION_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }
}

package ca.ulaval.glo4003.authentication;

import static io.restassured.RestAssured.given;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import io.restassured.http.Header;
import javax.ws.rs.core.Response;
import org.junit.Rule;
import org.junit.Test;

public class TokenRevocationResourceIT {

  private static final String REVOCATION_ROUTE = "/api/revoke-token";

  private static final String USERNAME = "a username";

  private static final String TOKEN = "a token";
  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();
  private Header userHeader = new Header("username", USERNAME);
  private Header tokenHeader = new Header("token", TOKEN);

  @Test
  public void givenNotAuthenticatedUser_whenRevokingToken_thenResponseIsUnauthorized() {
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
    .when()
        .post(REVOCATION_ROUTE)
    .then()
        .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());

    //@formatter:on
  }
}

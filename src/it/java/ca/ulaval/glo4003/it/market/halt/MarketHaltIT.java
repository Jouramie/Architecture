package ca.ulaval.glo4003.it.market.halt;

import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenAdministratorAlreadyAuthenticated;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

import ca.ulaval.glo4003.it.ResetServerBetweenTest;
import org.junit.ClassRule;
import org.junit.Test;

public class MarketHaltIT {

  public static final String API_HALT_MARKET_ROUTE = "/api/markets/%s/halt";
  private static final String INEXISTENT_MARKET = "market123";
  private static final String MARKET = "London";

  @ClassRule
  public static ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenHaltingMarket_thenReturnMarketStatus() {
    String token = givenAdministratorAlreadyAuthenticated();
    //@formatter:off
    given()
        .header("token", token)
        .queryParam("message", "foobar")
    .when()
        .post(String.format(API_HALT_MARKET_ROUTE, MARKET))
    .then()
        .statusCode(OK.getStatusCode())
        .body("market", equalTo(MARKET))
        .body("status", equalTo("HALTED"));
    //@formatter:on
  }

  @Test
  public void givenInexistentMarket_whenHaltingMarket_thenReturn404NotFound() {
    String token = givenAdministratorAlreadyAuthenticated();
    //@formatter:off
    given()
        .header("token", token)
        .queryParam("message", "foobar")
    .when()
        .post(String.format(API_HALT_MARKET_ROUTE, INEXISTENT_MARKET))
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenNonAdministratorUser_whenHaltingMarket_thenReturn401Unauthorized() {
    //@formatter:off
    given()
        .queryParam("message", "foobar")
    .when()
        .post(String.format(API_HALT_MARKET_ROUTE, MARKET))
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }
}

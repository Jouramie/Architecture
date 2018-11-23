package ca.ulaval.glo4003.market.halt;

import static ca.ulaval.glo4003.util.UserAuthenticationHelper.givenAdministratorAlreadyAuthenticated;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;
import org.junit.Test;

public class MarketResumeIT {

  private static final String INEXISTENT_MARKET = "market123";
  private static final String MARKET = "London";
  private static final String API_RESUME_MARKET_ROUTE = "/api/markets/%s/resume";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenResumingMarket_thenReturnMarketStatus() {
    String token = givenAdministratorAlreadyAuthenticated();
    givenHaltedMarket(token);
    //@formatter:off
    given()
        .header("token", token)
    .when()
        .post(String.format(API_RESUME_MARKET_ROUTE, MARKET))
    .then()
        .statusCode(OK.getStatusCode())
        .body("market", equalTo(MARKET))
        .body("status", equalTo("TRADING"));
    //@formatter:on
  }


  @Test
  public void givenInexistentMarket_whenResumingMarket_thenReturn404NotFound() {
    String token = givenAdministratorAlreadyAuthenticated();
    //@formatter:off
    given()
        .header("token", token)
    .when()
        .post(String.format(API_RESUME_MARKET_ROUTE, INEXISTENT_MARKET))
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenNonAdministratorUser_whenResumingMarketTrading_thenReturn401Unauthorized() {
    //@formatter:off
    given()
        .queryParam("message", "foobar")
    .when()
        .post(String.format(API_RESUME_MARKET_ROUTE, MARKET))
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  private void givenHaltedMarket(String token) {
    given()
        .header("token", token)
        .queryParam("message", "foobar")
        .when()
        .post(String.format(MarketHaltIT.API_HALT_MARKET_ROUTE, MARKET));
  }
}

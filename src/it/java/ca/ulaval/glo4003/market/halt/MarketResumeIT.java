package ca.ulaval.glo4003.market.halt;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.any;
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
    givenHaltedMarket();
    //@formatter:off
    when()
        .post(String.format(API_RESUME_MARKET_ROUTE, MARKET))
    .then()
        .statusCode(OK.getStatusCode())
        .body("market", equalTo(MARKET))
        .body("status", any(String.class));
    //@formatter:on
  }


  @Test
  public void givenInexistentMarket_whenResumingMarket_thenReturn404NotFound() {
    //@formatter:off
    when()
        .post(String.format(API_RESUME_MARKET_ROUTE, INEXISTENT_MARKET))
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenActiveMarket_whenResumingMarketTrading_thenReturn400MarketAlreadyActive() {
    //@formatter:off
    when()
        .post(String.format(API_RESUME_MARKET_ROUTE, MARKET))
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenNonAdministratorUser_whenResumingMarketTrading_thenReturn401Unauthorized() {
    //@formatter:off
    given()
        .queryParam("message", "foobar").when()
    .when()
        .post(String.format(API_RESUME_MARKET_ROUTE, MARKET))
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  private void givenHaltedMarket() {
    given().queryParam("message", "foobar").when().post(String.format(MarketHaltIT.API_HALT_MARKET_ROUTE, MARKET));
  }
}

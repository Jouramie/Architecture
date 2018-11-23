package ca.ulaval.glo4003.market.halt;

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

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenResumingMarket_thenReturnMarketStatus() {
    givenHaltedMarket();
    //@formatter:off
    given()
        .header("token", "00000000-0000-0000-0000-000000000000")
    .when()
        .post(String.format("/api/markets/%s/resume", MARKET))
    .then()
        .statusCode(OK.getStatusCode())
        .body("market", equalTo(MARKET))
        .body("status", equalTo("TRADING"));
    //@formatter:on
  }


  @Test
  public void givenInexistentMarket_whenResumingMarket_thenReturn404NotFound() {
    //@formatter:off
    given()
        .header("token", "00000000-0000-0000-0000-000000000000")
    .when()
        .post(String.format("/api/markets/%s/resume", INEXISTENT_MARKET))
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
        .post(String.format("/api/markets/%s/resume", MARKET))
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  private void givenHaltedMarket() {
    given()
        .header("token", "00000000-0000-0000-0000-000000000000")
        .queryParam("message", "foobar").when()
        .post(String.format("/api/markets/%s/halt", MARKET));
  }
}

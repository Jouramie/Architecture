package ca.ulaval.glo4003.market.halt;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;
import org.junit.Test;

public class MarketHaltIT {

  private static final String INEXISTENT_MARKET = "market123";
  private static final String MARKET = "London";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenHaltingMarket_thenReturnMarketStatus() {
    //@formatter:off
    given()
        .header("token", "00000000-0000-0000-0000-000000000000")
        .queryParam("message", "foobar")
    .when()
        .post(String.format("/markets/%s/halt", MARKET))
    .then()
        .statusCode(ACCEPTED.getStatusCode())
        .body("market", equalTo(MARKET))
        .body("status", any(String.class));
    //@formatter:on
  }

  @Test
  public void givenInexistentMarket_whenHaltingMarket_thenReturn404NotFound() {
    //@formatter:off
    given()
        .queryParam("message", "foobar")
    .when()
        .post(String.format("/markets/%s/halt", INEXISTENT_MARKET))
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
        .post(String.format("/markets/%s/halt", MARKET))
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }
}

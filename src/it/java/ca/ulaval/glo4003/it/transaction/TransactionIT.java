package ca.ulaval.glo4003.it.transaction;

import static ca.ulaval.glo4003.context.AbstractContext.DEFAULT_INVESTOR_EMAIL;
import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenAdministratorAlreadyAuthenticated;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasKey;

import ca.ulaval.glo4003.it.ResetServerBetweenTest;
import ca.ulaval.glo4003.it.portfolio.PortfolioReportITContext;
import io.restassured.http.Header;
import org.junit.ClassRule;
import org.junit.Test;

public class TransactionIT {
  private static final String SINCE_PARAM_NAME = "since";
  private static final String SINCE_LAST_FIVE_DAYS = "LAST_FIVE_DAYS";
  private static final String SINCE_LAST_THIRTY_DAYS = "LAST_THIRTY_DAYS";
  private static final String STOCK_TITLE = "MSFT";
  private static final String TYPE = "type";
  private static final String ITEMS = "items";
  private static final String TIMESTAMP = "timestamp";

  @ClassRule
  public static ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest(new PortfolioReportITContext());

  @Test
  public void whenGettingTransactions_thenReturnListOfTransactions() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam(SINCE_PARAM_NAME, SINCE_LAST_THIRTY_DAYS)
    .when()
        .get("/api/transactions")
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", everyItem(hasKey(TYPE)))
        .body("$", everyItem(hasKey(ITEMS)))
        .body("$", everyItem(hasKey(TIMESTAMP)));
    //@formatter:on
  }

  @Test
  public void givenUnauthenticatedUser_whenGettingTransactions_thenReturn401Unauthorized() {
    //@formatter:off
    given()
        .queryParam(SINCE_PARAM_NAME, SINCE_LAST_THIRTY_DAYS)
    .when()
        .get("/api/transactions")
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenMalformedScopeParameter_whenGettingTransactions_thenReturn400BadRequest() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam(SINCE_PARAM_NAME, "malformed")
    .when()
        .get("/api/transactions")
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void whenGettingUserTransactions_thenReturnListOfTransactions() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam(SINCE_PARAM_NAME, SINCE_LAST_THIRTY_DAYS)
    .when()
        .get("/api/users/{email}/transactions", DEFAULT_INVESTOR_EMAIL)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", everyItem(hasKey(TYPE)))
        .body("$", everyItem(hasKey(ITEMS)))
        .body("$", everyItem(hasKey(TIMESTAMP)));
    //@formatter:on
  }

  @Test
  public void givenUnauthenticatedUser_whenGettingUserTransactions_thenReturn401Unauthorized() {
    //@formatter:off
    given()
        .queryParam(SINCE_PARAM_NAME, SINCE_LAST_THIRTY_DAYS)
    .when()
        .get("/api/users/{email}/transactions", DEFAULT_INVESTOR_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenNonExistingUser_whenGettingUserTransactions_thenReturn404NotFound() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam(SINCE_PARAM_NAME, SINCE_LAST_FIVE_DAYS)
    .when()
        .get("/api/users/{email}/transactions", "not a valid email")
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenMalformedScopeParameter_whenGettingUserTransactions_thenReturn400BadRequest() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam(SINCE_PARAM_NAME, "malformed")
    .when()
        .get("/api/users/{email}/transactions", DEFAULT_INVESTOR_EMAIL)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void whenGettingStockTransactions_thenReturnListOfTransactions() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam(SINCE_PARAM_NAME, SINCE_LAST_THIRTY_DAYS)
    .when()
        .get("/api/stocks/{title}/transactions", STOCK_TITLE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", everyItem(hasKey(TYPE)))
        .body("$", everyItem(hasKey(ITEMS)))
        .body("$", everyItem(hasKey(TIMESTAMP)));
    //@formatter:on
  }

  @Test
  public void givenNonExistingStock_whenGettingStockTransactions_thenReturn404NotFound() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam(SINCE_PARAM_NAME, SINCE_LAST_FIVE_DAYS)
    .when()
        .get("/api/stocks/{title}/transactions", "not a valid title")
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenUnauthenticatedUser_whenGettingStockTransactions_thenReturn401Unauthorized() {
    //@formatter:off
    given()
        .queryParam(SINCE_PARAM_NAME, SINCE_LAST_THIRTY_DAYS)
    .when()
        .get("/api/stocks/{title}/transactions", STOCK_TITLE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenMalformedScopeParameter_whenGettingStockTransactions_thenReturn400BadRequest() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam(SINCE_PARAM_NAME, "malformed")
    .when()
        .get("/api/stocks/{title}/transactions", STOCK_TITLE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }
}

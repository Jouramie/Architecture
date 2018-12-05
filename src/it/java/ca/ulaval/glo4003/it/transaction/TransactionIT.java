package ca.ulaval.glo4003.it.transaction;

import static ca.ulaval.glo4003.context.AbstractContext.DEFAULT_ADMIN_EMAIL;
import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenAdministratorAlreadyAuthenticated;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.assertj.core.util.IterableUtil.iterable;
import static org.hamcrest.core.Is.is;

import ca.ulaval.glo4003.it.ResetServerBetweenTest;
import ca.ulaval.glo4003.ws.api.transaction.dto.TransactionModelDto;
import io.restassured.http.Header;
import org.junit.ClassRule;
import org.junit.Test;

public class TransactionIT {

  @ClassRule
  public static ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenGettingTransactions_thenReturnListOfTransactions() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam("since", "LAST_FIVE_DAYS")
    .when()
        .get("/api/transactions", DEFAULT_ADMIN_EMAIL)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(iterable(TransactionModelDto.class)));
    //@formatter:on
  }

  @Test
  public void givenUnauthenticatedUser_whenGettingTransactions_thenReturn401Unauthorized() {
    //@formatter:off
    given()
        .queryParam("since", "LAST_THIRTY_DAYS")
    .when()
        .get("/api/transactions", DEFAULT_ADMIN_EMAIL)
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
        .queryParam("since", "malformed")
    .when()
        .get("/api/transactions", DEFAULT_ADMIN_EMAIL)
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
        .queryParam("since", "LAST_FIVE_DAYS")
    .when()
        .get("/api/users/{email}/transactions", DEFAULT_ADMIN_EMAIL)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(iterable(TransactionModelDto.class)));
    //@formatter:on
  }

  @Test
  public void givenUnauthenticatedUser_whenGettingUserTransactions_thenReturn401Unauthorized() {
    //@formatter:off
    given()
        .queryParam("since", "LAST_THIRTY_DAYS")
    .when()
        .get("/api/users/{email}/transactions", DEFAULT_ADMIN_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenMalformedScopeParameter_whenGettingUserTransactions_thenReturn400BadRequest() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .queryParam("since", "malformed")
    .when()
        .get("/api/users/{email}/transactions", DEFAULT_ADMIN_EMAIL)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }
}

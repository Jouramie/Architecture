package ca.ulaval.glo4003.it.transaction;

import static ca.ulaval.glo4003.context.AbstractContext.DEFAULT_INVESTOR_EMAIL;
import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenAdministratorAlreadyAuthenticated;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
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
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenGettingUserTransactions_thenReturnListOfTransactions() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(String.format("/api/users/%s/transactions", DEFAULT_INVESTOR_EMAIL))
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(iterable(TransactionModelDto.class)));
    //@formatter:on
  }

  @Test
  public void givenUnauthenticatedUser_whenGettingTransactions_thenReturn401Unauthorized() {
    //@formatter:off
    when()
        .get(String.format("/api/users/%s/transactions", DEFAULT_INVESTOR_EMAIL))
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }
}

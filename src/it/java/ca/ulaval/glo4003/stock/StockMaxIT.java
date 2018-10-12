package ca.ulaval.glo4003.stock;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;
import org.junit.Test;

public class StockMaxIT {
  private static final String API_STOCK_ROUTE = "/api/stocks";
  private static final String API_MAX_ROUTE = "max";
  private static final String SINCE_PARAMETER = "since";

  private static final String TITLE = "title";
  private static final String MAX_VALUE = "maximumValue";
  private static final String MAX_VALUE_DATE = "maximumValueDate";

  private static final String SOME_TITLE = "RBS.l";
  private static final String SOME_SINCE_PARAMETER = "LAST_YEAR";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenGettingMaxValueOfStock_thenReturnTitleWithMaxValueAndDate() {
    //@formatter:off
    given()
        .param(SINCE_PARAMETER, SOME_SINCE_PARAMETER)
    .when()
        .get(API_STOCK_ROUTE + "/" + SOME_TITLE + "/" + API_MAX_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body(TITLE, equalTo(SOME_TITLE))
        .body(MAX_VALUE, any(Float.class))
        .body(MAX_VALUE_DATE, any(String.class));
    //@formatter:on
  }

  @Test
  public void whenGettingMaxValueOfNonExistentStock_thenReturn404() {
    //@formatter:off
    given()
        .param(SINCE_PARAMETER, SOME_SINCE_PARAMETER)
    .when()
        .get(API_STOCK_ROUTE + "/" + "wrong" + "/" + API_MAX_ROUTE)
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }

  @Test
  public void whenGettingMaxValueOfStockWithWrongSinceParameter_thenReturn400() {
    //@formatter:off
    given()
        .param(SINCE_PARAMETER, "wrong")
    .when()
        .get(API_STOCK_ROUTE + "/" + SOME_TITLE + "/" + API_MAX_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void whenGettingMaxValueOfStockWithoutSinceParameter_thenReturn400() {
    //@formatter:off
    when()
        .get(API_STOCK_ROUTE + "/" + SOME_TITLE + "/" + API_MAX_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }
}

package ca.ulaval.glo4003.it.stock;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

import ca.ulaval.glo4003.it.ResetServerBetweenTest;
import org.junit.ClassRule;
import org.junit.Test;

public class StockMaxIT {
  private static final String API_STOCK_ROUTE = "/api/stocks/%s/max";

  private static final String TITLE = "title";
  private static final String LAST_FIVE_DAYS = "lastFiveDays";
  private static final String CURRENT_MONTH = "currentMonth";
  private static final String LAST_MONTH = "lastMonth";
  private static final String LAST_YEAR = "lastYear";
  private static final String LAST_FIVE_YEARS = "lastFiveYears";
  private static final String LAST_TEN_YEARS = "lastTenYears";
  private static final String ALL_TIME = "allTime";
  private static final String MAX_VALUE = "maximumValue";
  private static final String MAX_VALUE_DATE = "maximumValueDate";

  private static final String SOME_TITLE = "RBS.l";
  private static final String WRONG_TITLE = "wrong";

  @ClassRule
  public static ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenGettingMaxValueOfStock_thenReturnTitleWithMaxValuesAndDates() {
    //@formatter:off
    given()
        .get(String.format(API_STOCK_ROUTE, SOME_TITLE))
    .then()
        .statusCode(OK.getStatusCode())
        .body(TITLE, equalTo(SOME_TITLE))
        .body(LAST_FIVE_DAYS + "." + MAX_VALUE, any(Float.class))
        .body(LAST_FIVE_DAYS + "." + MAX_VALUE_DATE, any(String.class))
        .body(CURRENT_MONTH + "." + MAX_VALUE, any(Float.class))
        .body(CURRENT_MONTH + "." + MAX_VALUE_DATE, any(String.class))
        .body(LAST_MONTH + "." + MAX_VALUE, any(Float.class))
        .body(LAST_MONTH + "." + MAX_VALUE_DATE, any(String.class))
        .body(LAST_YEAR + "." + MAX_VALUE, any(Float.class))
        .body(LAST_YEAR + "." + MAX_VALUE_DATE, any(String.class))
        .body(LAST_FIVE_YEARS + "." + MAX_VALUE, any(Float.class))
        .body(LAST_FIVE_YEARS + "." + MAX_VALUE_DATE, any(String.class))
        .body(LAST_TEN_YEARS + "." + MAX_VALUE, any(Float.class))
        .body(LAST_TEN_YEARS + "." + MAX_VALUE_DATE, any(String.class))
        .body(ALL_TIME + "." + MAX_VALUE, any(Float.class))
        .body(ALL_TIME + "." + MAX_VALUE_DATE, any(String.class));
    //@formatter:on
  }

  @Test
  public void whenGettingMaxValueOfNonExistentStock_thenReturn404() {
    //@formatter:off
    given()
        .get(String.format(API_STOCK_ROUTE, WRONG_TITLE))
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }
}

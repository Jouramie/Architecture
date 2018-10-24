package ca.ulaval.glo4003.stock;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.ClassRule;
import org.junit.Test;

public class StockIT {
  private static final String API_STOCK_ROUTE = "/api/stocks";
  private static final String API_STOCK_ROUTE_TITLE = "/api/stocks/%s";

  private static final String TITLE = "title";
  private static final String NAME = "name";
  private static final String MARKET = "market";
  private static final String CATEGORY = "category";
  private static final String OPEN_VALUE = "openValue";
  private static final String CURRENT_VALUE = "currentValue";
  private static final String CLOSE_VALUE = "closeValue";

  private static final String SOME_TITLE = "RBS.l";
  private static final String SOME_NAME = "Royal Bank of Scotland";
  private static final String SOME_MARKET = "London";
  private static final String SOME_CATEGORY = "Banking";

  @ClassRule
  public static ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenGettingByTitle_thenReturnStockInformation() {
    //@formatter:off
    when()
        .get(String.format(API_STOCK_ROUTE_TITLE, SOME_TITLE))
    .then()
        .statusCode(OK.getStatusCode())
        .body(TITLE, equalTo(SOME_TITLE))
        .body(NAME, equalTo(SOME_NAME))
        .body(MARKET, equalTo(SOME_MARKET))
        .body(CATEGORY, equalTo(SOME_CATEGORY))
        .body(OPEN_VALUE, any(Float.class))
        .body(CURRENT_VALUE, any(Float.class))
        .body(CLOSE_VALUE, any(Float.class));
    //@formatter:on
  }

  @Test
  public void givenWrongValue_whenGettingByTitle_thenStockIsNotFound() {
    //@formatter:off
    when()
        .get(String.format(API_STOCK_ROUTE_TITLE, "wrong"))
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }

  @Test
  public void whenGettingAll_thenReturnStocksInformation() {
    //@formatter:off
    when()
        .get(API_STOCK_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .header("X-Total-Count", is(notNullValue()))
        .body("$", everyItem(hasKey(TITLE)))
        .body("$", everyItem(hasKey(NAME)))
        .body("$", everyItem(hasKey(MARKET)))
        .body("$", everyItem(hasKey(CATEGORY)))
        .body("$", everyItem(hasKey(OPEN_VALUE)))
        .body("$", everyItem(hasKey(CURRENT_VALUE)))
        .body("$", everyItem(hasKey(CLOSE_VALUE)));
    //@formatter:on
  }

  @Test
  public void whenGettingByName_thenReturnSingleStockWithName() {
    //@formatter:off
    given()
        .param(NAME, SOME_NAME)
    .when()
        .get(API_STOCK_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .header("X-Total-Count", is("1"))
        .body("$", hasSize(1))
        .root("[0]")
        .body(TITLE, equalTo(SOME_TITLE))
        .body(NAME, equalTo(SOME_NAME))
        .body(MARKET, equalTo(SOME_MARKET))
        .body(CATEGORY, equalTo(SOME_CATEGORY))
        .body(OPEN_VALUE, any(Float.class))
        .body(CURRENT_VALUE, any(Float.class))
        .body(CLOSE_VALUE, any(Float.class));
    //@formatter:on
  }

  @Test
  public void givenWrongValue_whenGettingByName_thenReturnEmptyList() {
    //@formatter:off
    given()
        .param(NAME, "wrong")
    .when()
        .get(API_STOCK_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .header("X-Total-Count", is("0"))
        .body("$", is(emptyIterable()));
    //@formatter:on
  }

  @Test
  public void givenACategory_whenGettingByCategory_thenReturnStocksWithTheCategory() {
    //@formatter:off
    given()
        .param(CATEGORY, SOME_CATEGORY)
    .when()
        .get(API_STOCK_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .header("X-Total-Count", is(notNullValue()))
        .body("$", hasSize(greaterThanOrEqualTo(1)))
        .body("$", everyItem(hasEntry(CATEGORY, SOME_CATEGORY)));
    //@formatter:on
  }

  @Test
  public void givenWrongValue_whenGettingByCategory_thenReturnEmptyList() {
    //@formatter:off
    given()
        .param(CATEGORY, "wrong")
    .when()
        .get(API_STOCK_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .header("X-Total-Count", is("0"))
        .body("$", is(emptyIterable()));
    //@formatter:on
  }


  @Test
  public void given20PerPage_whenGettingAll_thenReturn20Stocks() {
    //@formatter:off
    when()
        .get(API_STOCK_ROUTE + "?per_page=20")
    .then()
        .statusCode(OK.getStatusCode())
        .header("X-Total-Count", is(notNullValue()))
        .body("$", hasSize(20));
    //@formatter:on
  }
}

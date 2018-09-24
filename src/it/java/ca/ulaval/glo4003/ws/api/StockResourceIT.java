package ca.ulaval.glo4003.ws.api;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;
import org.junit.Test;

public class StockResourceIT {
  private static final String API_STOCK_ROUTE = "/api/stocks";

  private static final String TITLE = "title";
  private static final String NAME = "name";
  private static final String MARKET = "market";
  private static final String CATEGORY = "category";
  private static final String OPEN = "open";
  private static final String CURRENT = "current";
  private static final String CLOSE = "close";

  private static final String SOME_TITLE = "RBS.l";
  private static final String SOME_NAME = "Royal Bank of Scotland";
  private static final String SOME_MARKET = "London";
  private static final String SOME_CATEGORY = "Banking";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenGettingByTitle_thenReturnStockInformation() {
    get(API_STOCK_ROUTE + "/" + SOME_TITLE)
        .then()
        .statusCode(200)
        .body(TITLE, equalTo(SOME_TITLE))
        .body(NAME, equalTo(SOME_NAME))
        .body(MARKET, equalTo(SOME_MARKET))
        .body(CATEGORY, equalTo(SOME_CATEGORY))
        .body(OPEN, any(Double.class))
        .body(CURRENT, any(Double.class))
        .body(CLOSE, any(Double.class));
  }

  @Test
  public void whenGettingByName_thenReturnStockInformation() {
    given()
        .param(NAME, SOME_NAME)
        .when()
        .get(API_STOCK_ROUTE)
        .then()
        .statusCode(200)
        .body(TITLE, equalTo(SOME_TITLE))
        .body(NAME, equalTo(SOME_NAME))
        .body(MARKET, equalTo(SOME_MARKET))
        .body(CATEGORY, equalTo(SOME_CATEGORY))
        .body(OPEN, any(Double.class))
        .body(CURRENT, any(Double.class))
        .body(CLOSE, any(Double.class));
  }

  @Test
  public void givenWrongValue_whenGettingByTitle_thenStockIsNotFound() {
    get(API_STOCK_ROUTE + "/wrong")
        .then().statusCode(404);
  }

  @Test
  public void givenWrongValue_whenGettingByName_thenStockIsNotFound() {
    given()
        .param(NAME, "wrong")
        .when()
        .get(API_STOCK_ROUTE)
        .then().statusCode(404);
  }

  @Test
  public void givenNoValue_whenGettingByName_thenBadRequest() {
    get(API_STOCK_ROUTE)
        .then().statusCode(400);
  }
}

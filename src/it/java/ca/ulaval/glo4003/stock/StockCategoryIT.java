package ca.ulaval.glo4003.stock;

import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.not;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;
import org.junit.Test;

public class StockCategoryIT {
  private static final String API_STOCK_CATEGORIES_ROUTE = "/api/stock_categories";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenGettingCategories_thenReturnCategories() {
    //@formatter:off
    when()
        .get(API_STOCK_CATEGORIES_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", not(emptyIterable()));
    //@formatter:on
  }
}

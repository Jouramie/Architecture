package ca.ulaval.glo4003.stock;

import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.any;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.domain.stock.StockTrend;
import org.junit.Rule;
import org.junit.Test;

public class StockTrendIT {
  private static final String STOCK_TREND_API_ROUTE = "/api/stocks/%s/trend";
  private static final String STOCK_TITLE = "RBS.l";
  private static final String INEXISTENT_STOCK_TITLE = "foobar";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenGettingStockVariationTrend_thenReturnVariationTrend() {
    //@formatter:off
    when()
        .get(String.format(STOCK_TREND_API_ROUTE, STOCK_TITLE))
    .then()
        .statusCode(OK.getStatusCode())
        .body("title", equalTo(STOCK_TITLE))
        .body("last5Days", any(StockTrend.class))
        .body("last30Days", any(StockTrend.class))
        .body("lastYear", any(StockTrend.class));
    //@formatter:on
  }

  @Test
  public void givenInexistentStockTitle_whenGettingStockVariationTrend_thenReturn404NotFound() {
    //@formatter:off
    when()
        .get(String.format(STOCK_TREND_API_ROUTE, INEXISTENT_STOCK_TITLE))
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }
}

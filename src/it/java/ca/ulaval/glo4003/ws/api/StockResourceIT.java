package ca.ulaval.glo4003.ws.api;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

public class StockResourceIT {
    private static final String API_STOCK_ROUTE = "/api/stock/";

    private static final String TITLE = "title";
    private static final String STOCK_NAME = "stockName";
    private static final String MARKET = "market";
    private static final String CATEGORY = "category";
    private static final String OPEN = "open";
    private static final String CURRENT = "current";
    private static final String CLOSE = "close";

    private static final String SOME_TITLE = "RBS.l";
    private static final String SOME_STOCK_NAME = "Royal Bank of Scotland";
    private static final String SOME_MARKET = "London";
    private static final String SOME_CATEGORY = "Banking";

    @Rule
    public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

    @Test
    public void whenGettingStockByTitle_thenReturnStockInformations() {
        get(API_STOCK_ROUTE  + TITLE + "/" + SOME_TITLE).
        then().
            statusCode(200).
            body(TITLE, equalTo(SOME_TITLE)).
            body(STOCK_NAME, equalTo(SOME_STOCK_NAME)).
            body(MARKET, equalTo(SOME_MARKET)).
            body(CATEGORY, equalTo(SOME_CATEGORY)).
            body(OPEN, any(Double.class)).
            body(CURRENT, any(Double.class)).
            body(CLOSE, any(Double.class));
    }

    @Test
    public void whenGettingStockByStockName_thenReturnStockInformations() {
        get(API_STOCK_ROUTE + STOCK_NAME + "/" + SOME_STOCK_NAME).
        then().
            statusCode(200).
            body(TITLE, equalTo(SOME_TITLE)).
            body(STOCK_NAME, equalTo(SOME_STOCK_NAME)).
            body(MARKET, equalTo(SOME_MARKET)).
            body(CATEGORY, equalTo(SOME_CATEGORY)).
            body(OPEN, any(Double.class)).
            body(CURRENT, any(Double.class)).
            body(CLOSE, any(Double.class));
    }

    @Test
    public void givenNoValue_whenGettingByTitle_thenBadRequest() {
        get(API_STOCK_ROUTE  + TITLE + "/").
        then().statusCode(400);
    }

    @Test
    public void givenNoValue_whenGettingByStockName_thenBadRequest() {
        get(API_STOCK_ROUTE  + STOCK_NAME + "/").
        then().statusCode(400);
    }

    @Test
    public void whenGettingStockWithWrongValue_thenStockIsNotFound() {
        get(API_STOCK_ROUTE  + TITLE + "/wrong").
        then().statusCode(404);
    }
}

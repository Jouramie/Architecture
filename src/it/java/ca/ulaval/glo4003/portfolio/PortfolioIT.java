package ca.ulaval.glo4003.portfolio;

import static ca.ulaval.glo4003.util.TestUserHelper.givenUserAlreadyAuthenticated;
import static ca.ulaval.glo4003.util.TestUserHelper.givenUserAlreadyRegistered;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.util.CartStockRequestBuilder;
import io.restassured.http.Header;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class PortfolioIT {
  private static final String API_PORTFOLIO_ROUTE = "/api/portfolio/";

  private static final String SOME_TITLE = "RBS.l";
  private static final int SOME_QUANTITY = 3;
  private static final String API_CART_ROUTE = "/api/cart/";
  private static final String API_CART_ROUTE_WITH_TITLE = API_CART_ROUTE + SOME_TITLE;
  private static final String API_CART_CHECKOUT_ROUTE = "/api/cart/checkout";

  private final CartStockRequestBuilder cartStockRequestBuilder = new CartStockRequestBuilder();

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenGetPortfolio_thenReturnStocksInPortfolio() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenPortfolioContainsDefaultStocks(tokenHeader);
    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_PORTFOLIO_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(iterableWithSize(1)))
        .body("[0].title", is(SOME_TITLE))
        .body("[0].quantity", is(SOME_QUANTITY));
    //@formatter:on
  }

  @Test
  public void givenEmptyPortfolio_whenGetPortfolio_thenReturnEmptyList() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_PORTFOLIO_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(emptyIterable()));
    //@formatter:on
  }

  @Test
  public void givenUserNotLoggedIn_whenGetPortfolio_thenReturnUnauthorized() {
    //@formatter:off
    when()
        .get(API_PORTFOLIO_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  private void givenPortfolioContainsDefaultStocks(Header tokenHeader) {
    //@formatter:off
    given()
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build(SOME_QUANTITY))
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_ROUTE_WITH_TITLE);

    given()
        .header(tokenHeader)
    .when()
        .post(API_CART_CHECKOUT_ROUTE);
    //@formatter:on
  }
}

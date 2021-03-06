package ca.ulaval.glo4003.it.cart;

import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenAdministratorAlreadyAuthenticated;
import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenInvestorAlreadyAuthenticated;
import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenInvestorAlreadyRegistered;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;

import ca.ulaval.glo4003.it.ResetServerBetweenTest;
import ca.ulaval.glo4003.it.market.halt.MarketHaltIT;
import ca.ulaval.glo4003.it.util.UserAuthenticationHelper;
import ca.ulaval.glo4003.ws.api.cart.CartStockRequestBuilder;
import ca.ulaval.glo4003.ws.api.user.StockLimitCreationRequestBuilder;
import io.restassured.http.Header;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class CartIT {
  private static final String SOME_TITLE = "RBS.l";
  private static final String SOME_MARKET = "London";
  private static final String SOME_TRANSACTION_TYPE = "PURCHASE";

  private static final String API_CART_ROUTE = "/api/cart/";
  private static final String API_CART_STOCK_ROUTE = "/api/cart/{title}";
  private static final String API_CART_CHECKOUT_ROUTE = "/api/cart/checkout";
  private static final String API_USERS_EMAIL_LIMIT_STOCK_ROUTE = "/api/users/{email}/limit/stock";

  private static final String TITLE = "title";
  private static final String MARKET = "market";
  private static final String CATEGORY = "category";
  private static final String CURRENT_VALUE = "currentValue";

  private static final String MONEY_AMOUNT = "moneyAmount";
  private static final String CURRENCY = "currency";
  private static final String QUANTITY = "quantity";
  private static final String HALT_MESSAGE = "market halted";


  private final CartStockRequestBuilder cartStockRequestBuilder = new CartStockRequestBuilder();

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void givenUserNotLoggedIn_whenGetCart_thenReturnUnauthorized() {
    //@formatter:off
    when()
        .get(API_CART_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvestorLoggedIn_whenGetCart_thenReturnEmptyList() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_CART_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(emptyIterable()));
    //@formatter:on
  }

  @Test
  public void givenUserNotLoggedIn_whenAddStockToCart_thenReturnUnauthorized() {
    //@formatter:off
    given()
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void whenAddStockToCart_thenReturnStockInList() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(iterableWithSize(1)))
        .body("[0].title", is(SOME_TITLE))
        .body("[0].quantity", is(CartStockRequestBuilder.DEFAULT_QUANTITY))
        .body("[0]", hasKey(MARKET))
        .body("[0]", hasKey(CATEGORY))
        .body("[0]", hasKey(CURRENT_VALUE));
    //@formatter:on
  }

  @Test
  public void givenEmptyCart_whenAddStockToCart_thenReturnStockInList() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(iterableWithSize(1)))
        .body("[0].title", is(SOME_TITLE))
        .body("[0].quantity", is(CartStockRequestBuilder.DEFAULT_QUANTITY))
        .body("[0]", hasKey(MARKET))
        .body("[0]", hasKey(CATEGORY))
        .body("[0]", hasKey(CURRENT_VALUE));
    //@formatter:on
  }

  @Test
  public void givenNegativeQuantityCartStockRequest_whenAddStockToCart_thenBadRequest() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .body(cartStockRequestBuilder.withQuantity(-1).build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenCartContainsDefaultStock_whenAddSameStockToCart_thenAddAmountOfStocks() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenCartContainsDefaultStock(tokenHeader);
    //@formatter:off
    given()
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("[0].quantity", is(CartStockRequestBuilder.DEFAULT_QUANTITY * 2));
    //@formatter:on
  }

  @Test
  public void givenUserNotLoggedIn_whenUpdateStockToCart_thenUnauthorized() {
    //@formatter:off
    given()
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .patch(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenEmptyCart_whenUpdateStockToCart_thenBadRequest() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .patch(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenNegativeQuantityCartStockRequest_whenUpdateStockToCart_thenBadRequest() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenCartContainsDefaultStock(tokenHeader);
    //@formatter:off
    given()
        .header(tokenHeader)
        .body(cartStockRequestBuilder.withQuantity(-1).build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .patch(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenUserNotLoggedIn_whenRemoveStockFromCart_thenReturnUnauthorized() {
    //@formatter:off
    given()
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .delete(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenCartContainsDefaultStock_whenRemoveStockFromCart_thenReturnStockInList() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenCartContainsDefaultStock(tokenHeader);
    //@formatter:off
    given()
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .delete(API_CART_STOCK_ROUTE, SOME_TITLE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(emptyIterable()));
    //@formatter:on
  }

  @Test
  public void givenUserNotLoggedIn_whenEmptyCart_thenReturnUnauthorized() {
    //@formatter:off
    when()
        .delete(API_CART_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenCartContainsDefaultStock_whenEmptyCart_thenEmptyTheCart() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenCartContainsDefaultStock(tokenHeader);
    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .delete(API_CART_ROUTE)
    .then()
        .statusCode(NO_CONTENT.getStatusCode());

    given()
        .header(tokenHeader)
    .when()
        .get(API_CART_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(emptyIterable()));
    //@formatter:on
  }

  @Test
  public void givenUserNotLoggedIn_whenCheckout_thenReturnUnauthorized() {
    //@formatter:off
    when()
        .post(API_CART_CHECKOUT_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenCartContainsDefaultStock_whenCheckout_thenReturnStockInList() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenCartContainsDefaultStock(tokenHeader);
    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .post(API_CART_CHECKOUT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("type", equalTo(SOME_TRANSACTION_TYPE))
        .body("items", is(iterableWithSize(1)))
        .body("items[0]", hasKey(TITLE))
        .body("items[0]", hasKey(QUANTITY))
        .body("items[0]", hasKey(MONEY_AMOUNT))
        .body("items[0]", hasKey(CURRENCY))
        .body("timestamp", any(Object.class));
    //@formatter:on
  }

  @Test
  public void givenEmptyCart_whenCheckout_thenReturnBadRequest() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .post(API_CART_CHECKOUT_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenSomeLimitAppliedToSomeUser_whenSomeUserBuyStockExceedingTheLimit_thenForbidden() {
    int maximalStockQuantity = 1;

    givenInvestorAlreadyRegistered();
    String administratorToken = givenAdministratorAlreadyAuthenticated();
    Header administratorTokenHeader = new Header("token", administratorToken);
    given().header(administratorTokenHeader).contentType(MediaType.APPLICATION_JSON)
        .body(new StockLimitCreationRequestBuilder().withStockQuantity(maximalStockQuantity).build())
        .when().put(API_USERS_EMAIL_LIMIT_STOCK_ROUTE, UserAuthenticationHelper.SOME_EMAIL);

    String investorToken = givenInvestorAlreadyAuthenticated();
    Header investorTokenHeader = new Header("token", investorToken);

    //@formatter:off
    given()
        .header(investorTokenHeader)
        .contentType(MediaType.APPLICATION_JSON)
        .body(new CartStockRequestBuilder().withQuantity(maximalStockQuantity + 1).build())
    .when()
        .put(API_CART_STOCK_ROUTE, SOME_TITLE);

    given()
        .header(investorTokenHeader)
    .when()
        .post(API_CART_CHECKOUT_ROUTE)
    .then()
        .statusCode(FORBIDDEN.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenCartContainsAStockWhichMarketIsHalted_whenCheckout_thenBadRequest() {
    givenInvestorAlreadyRegistered();
    String investorToken = givenInvestorAlreadyAuthenticated();
    String token = givenAdministratorAlreadyAuthenticated();
    givenHaltedMarket(token, SOME_MARKET);
    givenCartContainsDefaultStock(new Header("token", investorToken));

    //@formatter:off
    given()
        .header("token", investorToken)
    .when()
        .post(API_CART_CHECKOUT_ROUTE)
    .then()
        .body(containsString(HALT_MESSAGE))
        .statusCode(FORBIDDEN.getStatusCode());
    //@formatter:on
  }

  private void givenHaltedMarket(String token, String market) {
    //@formatter:off
    given()
        .header("token", token)
        .queryParam("message", HALT_MESSAGE)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(String.format(MarketHaltIT.API_HALT_MARKET_ROUTE, market));
    //@formatter:on
  }

  private void givenCartContainsDefaultStock(Header tokenHeader) {
    //@formatter:off
    given()
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_STOCK_ROUTE, SOME_TITLE);
    //@formatter:on
  }
}

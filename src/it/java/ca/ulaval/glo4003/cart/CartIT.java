package ca.ulaval.glo4003.cart;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.util.CartStockRequestBuilder;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import io.restassured.http.Header;
import io.restassured.response.Response;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class CartIT {
  private static final String SOME_TITLE = "RBS.l";

  private static final String API_CART_ROUTE = "/api/cart/";
  private static final String API_CART_ROUTE_WITH_TITLE = API_CART_ROUTE + SOME_TITLE;
  private static final String API_CART_CHECKOUT_ROUTE = "/api/cart/checkout";
  private static final String API_USERS_ROUTE = "/api/users";
  private static final String API_AUTHENTICATION_ROUTE = "/api/authenticate";

  private static final String TITLE = "title";
  private static final String NAME = "name";
  private static final String MARKET = "market";
  private static final String CATEGORY = "category";
  private static final String CURRENT_VALUE = "currentValue";
  private static final String QUANTITY = "quantity";

  private static final String SOME_USERNAME = "carticart";
  private static final String SOME_PASSWORD = "stockistock";

  private static final UserCreationDto A_CREATION_REQUEST =
      new UserCreationDto(SOME_USERNAME, SOME_PASSWORD, UserRole.INVESTOR);

  private static final AuthenticationRequestDto AN_AUTHENTICATION_REQUEST =
      new AuthenticationRequestDto(SOME_USERNAME, SOME_PASSWORD);

  private final Header userHeader = new Header("username", SOME_USERNAME);
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
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(userHeader)
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
        .put(API_CART_ROUTE_WITH_TITLE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void whenAddStockToCart_thenReturnStockInList() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_ROUTE_WITH_TITLE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(iterableWithSize(1)))
        .body("[0].title", is(SOME_TITLE))
        .body("[0].quantity", is(CartStockRequestBuilder.DEFAULT_QUANTITY))
        .body("[0]", hasKey(MARKET))
        .body("[0]", hasKey(CATEGORY))
        .body("[0]", hasKey(CURRENT_VALUE))
        .body("[0]", hasKey(QUANTITY));
    //@formatter:on
  }

  @Test
  public void givenEmptyCart_whenAddStockToCart_thenReturnStockInList() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_ROUTE_WITH_TITLE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(iterableWithSize(1)))
        .body("[0].title", is(SOME_TITLE))
        .body("[0].quantity", is(CartStockRequestBuilder.DEFAULT_QUANTITY))
        .body("[0]", hasKey(MARKET))
        .body("[0]", hasKey(CATEGORY))
        .body("[0]", hasKey(CURRENT_VALUE))
        .body("[0]", hasKey(QUANTITY));
    //@formatter:on
  }

  @Test
  public void givenCartContainsDefaultStock_whenAddSameStockToCart_thenAddAmountOfStocks() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenCartContainsDefaultStock(tokenHeader);
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_ROUTE_WITH_TITLE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("[0].quantity", is(CartStockRequestBuilder.DEFAULT_QUANTITY * 2));
    //@formatter:on
  }

  @Test
  public void givenUserNotLoggedIn_whenUpdateStockToCart_thenReturnUnauthorized() {
    //@formatter:off
    given()
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .patch(API_CART_ROUTE_WITH_TITLE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenEmptyCart_whenUpdateStockToCart_thenReturn400() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build(2))
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .patch(API_CART_ROUTE_WITH_TITLE)
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
        .delete(API_CART_ROUTE_WITH_TITLE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenCartContainsDefaultStock_whenRemoveStockFromCart_thenReturnStockInList() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenCartContainsDefaultStock(tokenHeader);
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .delete(API_CART_ROUTE_WITH_TITLE)
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
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenCartContainsDefaultStock(tokenHeader);
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
    .when()
        .delete(API_CART_ROUTE)
    .then()
        .statusCode(NO_CONTENT.getStatusCode());

    given()
        .header(userHeader)
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
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    givenCartContainsDefaultStock(tokenHeader);
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
    .when()
        .post(API_CART_CHECKOUT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body("$", is(iterableWithSize(1)))
        .body("$", everyItem(contains(hasProperty(TITLE))))
        .body("$", everyItem(contains(hasProperty(NAME))))
        .body("$", everyItem(contains(hasProperty(MARKET))))
        .body("$", everyItem(contains(hasProperty(CATEGORY))))
        .body("$", everyItem(contains(hasProperty(CURRENT_VALUE))))
        .body("$", everyItem(contains(hasProperty(QUANTITY))));
    //@formatter:on
  }

  @Test
  public void givenEmptyCart_whenCheckout_thenReturnBadRequest() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
    .when()
        .post(API_CART_CHECKOUT_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }


  private void givenUserAlreadyRegistered() {
    given().body(A_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON).post(API_USERS_ROUTE);
  }

  private String givenUserAlreadyAuthenticated() {
    //@formatter:off
    Response response = given()
        .body(AN_AUTHENTICATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_AUTHENTICATION_ROUTE);
    //@formatter:on

    return response.jsonPath().getString("token");
  }

  private void givenCartContainsDefaultStock(Header tokenHeader) {
    //@formatter:off
    given()
        .header(userHeader)
        .header(tokenHeader)
        .body(cartStockRequestBuilder.build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_CART_ROUTE_WITH_TITLE);
    //@formatter:on
  }
}

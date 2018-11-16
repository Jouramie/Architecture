package ca.ulaval.glo4003.user;

import static ca.ulaval.glo4003.util.UserAuthenticationHelper.givenAdministratorAlreadyAuthenticated;
import static ca.ulaval.glo4003.util.UserAuthenticationHelper.givenInvestorAlreadyAuthenticated;
import static ca.ulaval.glo4003.util.UserAuthenticationHelper.givenInvestorAlreadyRegistered;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserMoneyAmountLimitCreationDto;
import io.restassured.http.Header;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class UserIT {

  private static final String SOME_EMAIL = "john.smith@investul.com";
  private static final String SOME_PASSWORD = "password";
  private static final UserCreationDto SOME_USER_CREATION_REQUEST = new UserCreationDto(SOME_EMAIL, SOME_PASSWORD);

  private static final String API_USERS_ROUTE = "/api/users";
  private static final String API_USERS_EMAIL_ROUTE = "/api/users/%s";
  private static final String API_USERS_EMAIL_LIMIT_ROUTE = "/api/users/%s/limit";
  private static final String API_USERS_EMAIL_LIMIT_STOCK_ROUTE = "/api/users/%s/limit/stock";
  private static final String API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE = "/api/users/%s/limit/money_amount";

  private static final String EMAIL = "email";
  private static final String ROLE = "role";
  private static final String LIMIT = "limit";
  private static final String INPUT_ERRORS = "inputErrors";
  private static final String MAXIMAL_STOCK_QUANTITY = "maximalStockQuantity";
  private static final String BEGIN_DATE = "beginDate";
  private static final String END_DATE = "endDate";

  private static final UserRole INVESTOR_USER_ROLE = UserRole.INVESTOR;

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  private static void givenSomeUserCreated() {
    given().body(SOME_USER_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON).post(API_USERS_ROUTE);
  }

  private static void givenSomeLimitAddedToSomeUser(String token, UserMoneyAmountLimitCreationDto request) {
    Header tokenHeader = new Header("token", token);
    given().header(tokenHeader).body(request).contentType(MediaType.APPLICATION_JSON).when()
        .put(API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE, SOME_EMAIL);
  }

  @Test
  public void whenCreatingUser_thenReturnCreatedUserInformation() {
    //@formatter:off
    given()
        .body(SOME_USER_CREATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_USERS_ROUTE)
    .then()
        .statusCode(CREATED.getStatusCode())
        .body(EMAIL, equalTo(SOME_EMAIL))
        .body(ROLE, equalTo(INVESTOR_USER_ROLE.toString()))
        .body("$", not(hasKey(LIMIT)));
    //@formatter:on
  }

  @Test
  public void givenSomeUserCreated_whenCreatingTheSameUser_thenBadRequest() {
    givenSomeUserCreated();

    //@formatter:off
    given()
        .body(SOME_USER_CREATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_USERS_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenEmptyEmail_whenCreatingUser_thenBadRequest() {
    //@formatter:off
    given()
        .body(new UserCreationDto("", SOME_PASSWORD))
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_USERS_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode())
        .body(INPUT_ERRORS, any(List.class));
    //@formatter:on
  }

  @Test
  public void givenTooSmallPassword_whenCreatingUser_thenBadRequest() {
    //@formatter:off
    given()
        .body(new UserCreationDto(SOME_EMAIL, "1234567"))
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_USERS_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode())
        .body(INPUT_ERRORS, any(List.class));
    //@formatter:on
  }

  @Test
  public void givenSomeUserCreated_whenGetTheUser_thenReturnTheUser() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_USERS_EMAIL_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(OK.getStatusCode())
        .body(EMAIL, equalTo(SOME_EMAIL))
        .body(ROLE, equalTo(INVESTOR_USER_ROLE))
        .body("$", not(hasKey(LIMIT)));
    //@formatter:on
  }

  @Test
  public void givenSomeUserNotCreated_whenGetTheUser_thenNotFound() {
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_USERS_EMAIL_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(NOT_FOUND.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenAdministratorNotLoggedIn_whenGetUser_thenUnauthorized() {
    //@formatter:off
    when()
        .get(API_USERS_EMAIL_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvestorLoggedIn_whenGetUser_thenUnauthorized() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_USERS_EMAIL_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenSomeUserCreated_whenGetAllUsers_thenReturnTheUser() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_USERS_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .header("X-Total-Count", is(notNullValue()))
        .body("$", everyItem(hasKey(EMAIL)))
        .body("$", everyItem(hasKey(ROLE)));
    //@formatter:on
  }

  @Test
  public void givenAdministratorNotLoggedIn_whenGetAllUsers_thenUnauthorized() {
    //@formatter:off
    when()
        .get(API_USERS_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvestorLoggedIn_whenGetAllUsers_thenUnauthorized() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_USERS_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenSomeUserCreated_whenPutSomeStockLimit_thenCreated() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    int stockLimit = 5;

    //@formatter:off
    given()
        .header(tokenHeader)
        .body(new StockLimitCreationRequestBuilder().withStockQuantity(stockLimit).build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_USERS_EMAIL_LIMIT_STOCK_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(CREATED.getStatusCode())
        .body("$", hasKey("beginDate"))
        .body("$", hasKey("endDate"))
        .body(MAXIMAL_STOCK_QUANTITY, is(5));
    //@formatter:on
  }

  @Test
  public void givenSomeUserCreated_whenPutSomeMoneyAmountLimit_thenCreated() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    double moneyAmountLimit = 99.99;

    //@formatter:off
    given()
        .header(tokenHeader)
        .body(new MoneyAmountLimitCreationRequestBuilder().withMoneyAmount(moneyAmountLimit).build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(CREATED.getStatusCode())
        .body("$", hasKey(BEGIN_DATE))
        .body("$", hasKey(END_DATE))
        .body(MAXIMAL_STOCK_QUANTITY, is(moneyAmountLimit));
    //@formatter:on
  }

  @Test
  public void givenSomeLimitAddedToSomeUser_whenGetTheUser_thenUserHasLimit() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    double moneyAmountLimit = 12.34;
    givenSomeLimitAddedToSomeUser(token,
        new MoneyAmountLimitCreationRequestBuilder().withMoneyAmount(moneyAmountLimit).build());

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_USERS_EMAIL_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(OK.getStatusCode())
        .root(LIMIT)
        .body("$", hasKey(BEGIN_DATE))
        .body("$", hasKey(END_DATE))
        .body(MAXIMAL_STOCK_QUANTITY, is(moneyAmountLimit));
    //@formatter:on
  }

  @Test
  public void givenAdministratorNotLoggedIn_whenPutSomeStockLimit_thenUnauthorized() {
    //@formatter:off
    given()
        .body(new StockLimitCreationRequestBuilder().build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_USERS_EMAIL_LIMIT_STOCK_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvestorLoggedIn_whenPutSomeStockLimit_thenUnauthorized() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
        .body(new StockLimitCreationRequestBuilder().build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_USERS_EMAIL_LIMIT_STOCK_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenAdministratorNotLoggedIn_whenPutSomeMoneyAmountLimit_thenUnauthorized() {
    //@formatter:off
    given()
        .body(new MoneyAmountLimitCreationRequestBuilder().build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvestorLoggedIn_whenPutSomeMoneyAmountLimit_thenUnauthorized() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
        .body(new MoneyAmountLimitCreationRequestBuilder().build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenSomeLimitAddedToSomeUser_whenDeleteTheUserLimit_thenNoContent() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    givenSomeLimitAddedToSomeUser(token, new MoneyAmountLimitCreationRequestBuilder().build());

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .delete(API_USERS_EMAIL_LIMIT_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(NO_CONTENT.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenAdministratorNotLoggedIn_whenDeleteSomeUserLimit_thenUnauthorized() {
    //@formatter:off
    when()
        .delete(API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvestorLoggedIn_whenDeleteSomeUserLimit_thenUnauthorized() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .delete(API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }
}

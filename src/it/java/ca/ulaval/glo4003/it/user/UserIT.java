package ca.ulaval.glo4003.it.user;

import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenAdministratorAlreadyAuthenticated;
import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenInvestorAlreadyAuthenticated;
import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenInvestorAlreadyRegistered;
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

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.it.ResetServerBetweenTest;
import ca.ulaval.glo4003.ws.api.user.MoneyAmountLimitCreationRequestBuilder;
import ca.ulaval.glo4003.ws.api.user.StockLimitCreationRequestBuilder;
import ca.ulaval.glo4003.ws.api.user.dto.InvestorCreationDto;
import ca.ulaval.glo4003.ws.api.user.dto.MoneyAmountLimitCreationDto;
import io.restassured.http.Header;
import java.math.BigDecimal;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class UserIT {
  private static final String SOME_EMAIL = "john.smith@investul.com";
  private static final String SOME_PASSWORD = "password";
  private static final InvestorCreationDto SOME_USER_CREATION_REQUEST = new InvestorCreationDto(SOME_EMAIL, SOME_PASSWORD);

  private static final String API_USERS_ROUTE = "/api/users";
  private static final String API_USERS_EMAIL_ROUTE = "/api/users/{email}";
  private static final String API_USERS_EMAIL_LIMIT_ROUTE = "/api/users/{email}/limit";
  private static final String API_USERS_EMAIL_LIMIT_STOCK_ROUTE = "/api/users/{email}/limit/stock";
  private static final String API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE = "/api/users/{email}/limit/money_amount";

  private static final String EMAIL = "email";
  private static final String ROLE = "role";
  private static final String LIMIT = "limit";
  private static final String INPUT_ERRORS = "inputErrors";
  private static final String STOCK_QUANTITY = "stockQuantity";
  private static final String MONEY_AMOUNT = "moneyAmount";
  private static final String BEGIN_DATE = "begin";
  private static final String END_DATE = "end";

  private static final String INVESTOR_USER_ROLE = UserRole.INVESTOR.toString();

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  private static void givenSomeUserCreated() {
    given().body(SOME_USER_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON).post(API_USERS_ROUTE);
  }

  private static void givenSomeLimitAddedToSomeUser(String token, MoneyAmountLimitCreationDto request) {
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
        .body(ROLE, equalTo(INVESTOR_USER_ROLE))
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
        .body(new InvestorCreationDto("", SOME_PASSWORD))
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_USERS_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenTooSmallPassword_whenCreatingUser_thenBadRequest() {
    //@formatter:off
    given()
        .body(new InvestorCreationDto(SOME_EMAIL, "1234567"))
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_USERS_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
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
  public void givenSomeUserCreated_whenGetAllUsers_thenReturnUsers() {
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
        .body("$", hasKey(BEGIN_DATE))
        .body("$", hasKey(END_DATE))
        .body(STOCK_QUANTITY, is(5));
    //@formatter:on
  }

  @Test
  public void givenSomeUserCreated_whenPutSomeMoneyAmountLimit_thenCreated() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    float moneyAmountLimit = 99.99f;

    //@formatter:off
    given()
        .header(tokenHeader)
        .body(new MoneyAmountLimitCreationRequestBuilder().withMoneyAmount(BigDecimal.valueOf(moneyAmountLimit)).build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(CREATED.getStatusCode())
        .body("$", hasKey(BEGIN_DATE))
        .body("$", hasKey(END_DATE))
        .body(MONEY_AMOUNT, is(moneyAmountLimit));
    //@formatter:on
  }

  @Test
  public void givenSomeLimitAddedToSomeUser_whenGetTheUser_thenUserHasLimit() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    float moneyAmountLimit = 12.34f;
    givenSomeLimitAddedToSomeUser(token,
        new MoneyAmountLimitCreationRequestBuilder().withMoneyAmount(BigDecimal.valueOf(moneyAmountLimit)).build());

    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_USERS_EMAIL_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(OK.getStatusCode())
        .body(LIMIT, hasKey(BEGIN_DATE))
        .body(LIMIT, hasKey(END_DATE))
        .body(LIMIT + "." + MONEY_AMOUNT, is(moneyAmountLimit));
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
        .delete(API_USERS_EMAIL_LIMIT_ROUTE, SOME_EMAIL)
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
        .delete(API_USERS_EMAIL_LIMIT_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenNegativeMoneyAmount_whenPutLimitSomeUser_thenBadRequest() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
        .body(new MoneyAmountLimitCreationRequestBuilder().withMoneyAmount(BigDecimal.valueOf(-1)).build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_USERS_EMAIL_LIMIT_MONEY_AMOUNT_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenNegativeStockQuantity_whenPutLimitSomeUser_thenBadRequest() {
    givenSomeUserCreated();
    String token = givenAdministratorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);

    //@formatter:off
    given()
        .header(tokenHeader)
        .body(new StockLimitCreationRequestBuilder().withStockQuantity(-1).build())
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .put(API_USERS_EMAIL_LIMIT_STOCK_ROUTE, SOME_EMAIL)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }
}

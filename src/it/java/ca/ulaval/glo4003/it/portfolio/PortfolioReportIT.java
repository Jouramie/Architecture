package ca.ulaval.glo4003.it.portfolio;

import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenInvestorAlreadyAuthenticated;
import static ca.ulaval.glo4003.it.util.UserAuthenticationHelper.givenInvestorAlreadyRegistered;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.iterableWithSize;

import ca.ulaval.glo4003.context.DemoContext;
import ca.ulaval.glo4003.context.JerseyApiHandlersCreator;
import ca.ulaval.glo4003.context.SwaggerApiHandlerCreator;
import ca.ulaval.glo4003.domain.clock.ReadableClock;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.it.ResetServerBetweenTest;
import io.restassured.http.Header;
import java.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;

public class PortfolioReportIT {
  private static final String API_REPORT_ROUTE = "/api/portfolio/report/";

  private static final String SINCE = "since";
  private static final String LAST_FIVE_DAYS = "LAST_FIVE_DAYS";

  private static final String HISTORY_KEY = "history";
  private static final String DATE_KEY = "date";
  private static final String STOCKS_KEY = "stocks";
  private static final String TOTAL_VALUE_KEY = "totalValue";
  private static final String INCREASING_STOCK_KEY = "mostIncreasingStock";
  private static final String DECREASING_STOCK_KEY = "mostDecreasingStock";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest(new PortfolioReportITContext(new JerseyApiHandlersCreator()));

  @Test
  public void givenPortfolioWithStocksInRequestedRange_whenGetPortfolioReport_thenReturnPortfolioHistory() {
    LocalDate currentDate = ServiceLocator.INSTANCE.get(ReadableClock.class).getCurrentTime().toLocalDate();

    Header tokenHeader = new Header("token", DemoContext.DEFAULT_INVESTOR_TOKEN);
    //@formatter:off
    given()
        .header(tokenHeader)
        .param(SINCE, LAST_FIVE_DAYS)
    .when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body(HISTORY_KEY, is(iterableWithSize(6)))
        .body(HISTORY_KEY + "[0]." + DATE_KEY, is(currentDate.minusDays(5).toString()))
        .body(HISTORY_KEY + "[0]." + STOCKS_KEY, is(iterableWithSize(2)))
        .body(HISTORY_KEY + "[0]." + TOTAL_VALUE_KEY, greaterThan(0f));
    //@formatter:on
  }

  @Test
  public void givenPortfolioContainedStocksInRequestedRange_whenGetPortfolioReport_thenReturnStocksWithGreatestVariation() {
    Header tokenHeader = new Header("token", DemoContext.DEFAULT_INVESTOR_TOKEN);
    //@formatter:off
    given()
        .header(tokenHeader)
        .param(SINCE, LAST_FIVE_DAYS)
    .when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body(DECREASING_STOCK_KEY, isOneOf("RBS.l", "AAPL", "MSFT"))
        .body(INCREASING_STOCK_KEY, isOneOf("RBS.l", "AAPL", "MSFT"));
    //@formatter:on
  }

  @Test
  public void givenTransactionsWereMadeInTimeRange_whenGetPortfolioReport_thenPortfolioContentIsVarying() {
    Header tokenHeader = new Header("token", DemoContext.DEFAULT_INVESTOR_TOKEN);
    //@formatter:off
    given()
        .header(tokenHeader)
        .param(SINCE, LAST_FIVE_DAYS)
    .when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body(HISTORY_KEY + "[0]." + STOCKS_KEY, is(iterableWithSize(2)))
        .body(HISTORY_KEY + "[5]." + STOCKS_KEY, is(iterableWithSize(3)));
    //@formatter:on
  }

  @Test
  public void givenEmptyPortfolio_whenGetPortfolioReport_thenReturnEmptyHistory() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .param(SINCE, LAST_FIVE_DAYS)
    .when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body(HISTORY_KEY, is(iterableWithSize(6)))
        .body(HISTORY_KEY + "[0]." + TOTAL_VALUE_KEY, equalTo(0f))
        .body(HISTORY_KEY + "[0]." + STOCKS_KEY, is(iterableWithSize(0)));
    //@formatter:on
  }

  @Test
  public void givenEmptyPortfolio_whenGetPortfolioReport_thenStocksWithGreatestVariationAreEmpty() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .param(SINCE, LAST_FIVE_DAYS)
    .when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body(INCREASING_STOCK_KEY, isEmptyOrNullString())
        .body(DECREASING_STOCK_KEY, isEmptyOrNullString());
    //@formatter:on
  }

  @Test
  public void givenUserNotLoggedIn_whenGetPortfolioReport_thenReturnUnauthorized() {
    //@formatter:off
    given()
        .param(SINCE, LAST_FIVE_DAYS)
    .when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenNoSinceParam_whenGetPortfolioReport_thenReturnBadRequest() {
    givenInvestorAlreadyRegistered();
    String token = givenInvestorAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
    .when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }
}

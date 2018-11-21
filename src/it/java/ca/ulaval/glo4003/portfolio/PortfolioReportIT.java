package ca.ulaval.glo4003.portfolio;

import static ca.ulaval.glo4003.util.TestUserHelper.givenUserAlreadyAuthenticated;
import static ca.ulaval.glo4003.util.TestUserHelper.givenUserAlreadyRegistered;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.iterableWithSize;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
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
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest(new PortfolioReportITContext());

  @Test
  public void givenPortfolioContainedStocksInRequestedRange_whenGetPortfolioReport_thenReturnPortfolioHistory() {
    LocalDate currentDate = ServiceLocator.INSTANCE.get(Clock.class).getCurrentTime().toLocalDate();

    String token = "00000000-0000-0000-0000-000000000000";
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
        .body(HISTORY_KEY + "[0]." + DATE_KEY, is(currentDate.minusDays(5).toString()))
        .body(HISTORY_KEY + "[0]." + STOCKS_KEY, is(iterableWithSize(2)))
        .body(HISTORY_KEY + "[0]." + TOTAL_VALUE_KEY, greaterThan(0f));
    //@formatter:on
  }

  @Test
  public void givenPortfolioContainedStocksInRequestedRange_whenGetPortfolioReport_thenReturnStocksWithGreatestVariation() {
    String token = "00000000-0000-0000-0000-000000000000";
    Header tokenHeader = new Header("token", token);
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
    String token = "00000000-0000-0000-0000-000000000000";
    Header tokenHeader = new Header("token", token);
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
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .param(SINCE, LAST_FIVE_DAYS)
    .when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body(HISTORY_KEY, is(iterableWithSize(0)));
    //@formatter:on
  }

  @Test
  public void givenEmptyPortfolio_whenGetPortfolioReport_thenStocksWithGreatestVariationAreEmpty() {
    givenUserAlreadyRegistered();
    String token = givenUserAlreadyAuthenticated();
    Header tokenHeader = new Header("token", token);
    //@formatter:off
    given()
        .header(tokenHeader)
        .param(SINCE, LAST_FIVE_DAYS)
    .when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(OK.getStatusCode())
        .body(INCREASING_STOCK_KEY, isEmptyString())
        .body(DECREASING_STOCK_KEY, isEmptyString());
    //@formatter:on
  }

  @Test
  public void givenUserNotLoggedIn_whenGetPortfolioReport_thenReturnUnauthorized() {
    //@formatter:off
    when()
        .get(API_REPORT_ROUTE)
    .then()
        .statusCode(UNAUTHORIZED.getStatusCode());
    //@formatter:on
  }
}

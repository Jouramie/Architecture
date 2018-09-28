package ca.ulaval.glo4003.ws.api;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;
import org.junit.Test;

public class PingResourceIT {
  private static final String API_PING_ROUTE = "/api/ping";

  private static final String SOME_ECHO = "Hello world!";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenPinging_thenApplicationRespondWithEchoMessage() {
    //@formatter:off
    given()
        .param("echo", SOME_ECHO)
    .when()
        .get(API_PING_ROUTE)
    .then()
        .statusCode(200)
        .body("version", any(String.class))
        .body("date", any(Object.class))
        .body("echo", equalTo(SOME_ECHO));
    //@formatter:on
  }

  @Test
  public void whenPingingWithoutEchoQueryParam_thenBadRequest() {
    //@formatter:off
    when()
        .get(API_PING_ROUTE)
    .then()
        .statusCode(400);
    //@formatter:on
  }
}

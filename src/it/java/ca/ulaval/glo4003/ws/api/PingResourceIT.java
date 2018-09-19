package ca.ulaval.glo4003.ws.api;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

public class PingResourceIT {
    private static final String API_PING_ROUTE = "/api/ping";

    private static final String SOME_ECHO = "Hello world!";

    @Rule
    public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

    @Test
    public void whenPinging_thenApplicationRespondWithEchoMessage() {
        given().
            param("echo", SOME_ECHO).
        when().
            get(API_PING_ROUTE).
        then().
            statusCode(200).
            body("version", any(String.class)).
            body("date", any(String.class)).
            body("echo", equalTo(SOME_ECHO));
    }

    @Test
    public void whenPingingWithoutEchoQueryParam_thenBadRequest() {
        get(API_PING_ROUTE).then().statusCode(400);
    }
}

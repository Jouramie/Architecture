package ca.ulaval.glo4003.ws.api;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import org.junit.Rule;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

public class PingResourceIT {

    @Rule
    public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

    @Test
    public void givenApplicationBooted_whenPinging_thenApplicationRespondWithEchoMessage() {
        given().
            param("echo", "Hello world!").
        when().
            get("/api/ping").
        then().
            statusCode(200).
            body("version", any(String.class)).
            body("date", any(String.class)).
            body("echo", equalTo("Hello world!"));
    }

    @Test
    public void givenApplicationBooted_whenPingingWithoutEchoQueryParam_thenApplicationRespondWith400() {
        get("/api/ping").then().statusCode(400);
    }
}

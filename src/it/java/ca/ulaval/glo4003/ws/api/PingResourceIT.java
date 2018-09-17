package ca.ulaval.glo4003.ws.api;

import static org.hamcrest.Matchers.any;
import org.junit.Test;

import static io.restassured.RestAssured.get;

public class PingResourceIT {

    @Test
    public void givenApplicationBooted_whenPinging_thenApplicationRespondWithVersion() {
        get("/api/ping").then().body("version", any(String.class));
    }
}

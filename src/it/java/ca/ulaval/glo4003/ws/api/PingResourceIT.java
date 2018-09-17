package ca.ulaval.glo4003.ws.api;

import ca.ulaval.glo4003.StartServerRule;
import org.junit.Rule;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.any;

public class PingResourceIT {

    @Rule
    public StartServerRule startServer = new StartServerRule();

    @Test
    public void givenApplicationBooted_whenPinging_thenApplicationRespondWithVersion() {
        get("/api/ping").then().body("version", any(String.class));
    }
}

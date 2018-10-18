package ca.ulaval.glo4003.util;

import static io.restassured.RestAssured.given;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import io.restassured.response.Response;
import javax.ws.rs.core.MediaType;

public class TestUserHelper {
  private static final String API_USERS_ROUTE = "/api/users";
  private static final String API_AUTHENTICATION_ROUTE = "/api/authenticate";

  private static final String SOME_EMAIL = "carticart";
  private static final String SOME_PASSWORD = "stockistock";

  private static final UserCreationDto A_CREATION_REQUEST =
      new UserCreationDto(SOME_EMAIL, SOME_PASSWORD);
  private static final AuthenticationRequestDto AN_AUTHENTICATION_REQUEST =
      new AuthenticationRequestDto(SOME_EMAIL, SOME_PASSWORD);

  public static String givenUserAlreadyAuthenticated() {
    //@formatter:off
    Response response = given()
        .body(AN_AUTHENTICATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_AUTHENTICATION_ROUTE);
    //@formatter:on

    return response.jsonPath().getString("token");
  }

  public static void givenUserAlreadyRegistered() {
    given().body(A_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON).post(API_USERS_ROUTE);
  }
}

package ca.ulaval.glo4003.util;

import static io.restassured.RestAssured.given;

import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import io.restassured.response.Response;
import javax.ws.rs.core.MediaType;

public class UserAuthenticationHelper {
  public static final String SOME_EMAIL = "carticart@investul.ca";

  private static final ApiAuthenticationRequestDto SOME_ADMINISTRATOR_AUTHENTICATION_REQUEST =
      new ApiAuthenticationRequestDto("Archi.test.43@gmail.com", "asdfasdf");
  private static final String API_USERS_ROUTE = "/api/users";
  private static final String API_AUTHENTICATION_ROUTE = "/api/authenticate";
  private static final String SOME_PASSWORD = "stockistock";
  private static final UserCreationDto SOME_USER_CREATION_REQUEST =
      new UserCreationDto(SOME_EMAIL, SOME_PASSWORD);
  private static final ApiAuthenticationRequestDto SOME_USER_AUTHENTICATION_REQUEST =
      new ApiAuthenticationRequestDto(SOME_EMAIL, SOME_PASSWORD);

  public static String givenInvestorAlreadyAuthenticated() {
    //@formatter:off
    Response response = given()
        .body(SOME_USER_AUTHENTICATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_AUTHENTICATION_ROUTE);
    //@formatter:on

    return response.jsonPath().getString("token");
  }

  public static void givenInvestorAlreadyRegistered() {
    given().body(SOME_USER_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON).post(API_USERS_ROUTE);
  }

  public static String givenAdministratorAlreadyAuthenticated() {
    //@formatter:off
    Response response = given()
        .body(SOME_ADMINISTRATOR_AUTHENTICATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_AUTHENTICATION_ROUTE);
    //@formatter:on

    return response.jsonPath().getString("token");
  }
}

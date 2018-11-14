package ca.ulaval.glo4003.user;

import static ca.ulaval.glo4003.util.UserAuthenticationHelper.givenAdministratorAlreadyAuthenticated;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.ws.api.user.dto.UserCreationDto;
import io.restassured.http.Header;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class UserIT {

  private static final String SOME_EMAIL = "john.smith@investul.com";
  private static final String SOME_PASSWORD = "password";
  private static final UserCreationDto SOME_USER_CREATION_REQUEST =
      new UserCreationDto(SOME_EMAIL, SOME_PASSWORD);

  private static final String API_USERS_ROUTE = "/api/users";
  private static final String API_USERS_EMAIL_ROUTE = "/api/users/%s";

  private static final String EMAIL = "email";
  private static final String ROLE = "role";
  private static final String LIMIT = "limit";
  private static final String INPUT_ERRORS = "inputErrors";

  private static final UserRole INVESTOR_USER_ROLE = UserRole.INVESTOR;

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  private static void givenSomeUserCreated() {
    given().body(SOME_USER_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON).post(API_USERS_ROUTE);
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
        .body(ROLE, equalTo(INVESTOR_USER_ROLE.toString()))
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
        .body(new UserCreationDto("", SOME_PASSWORD))
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_USERS_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode())
        .body(INPUT_ERRORS, any(List.class));
    //@formatter:on
  }

  @Test
  public void givenTooSmallPassword_whenCreatingUser_thenBadRequest() {
    //@formatter:off
    given()
        .body(new UserCreationDto(SOME_EMAIL, "1234567"))
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(API_USERS_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode())
        .body(INPUT_ERRORS, any(List.class));
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
}

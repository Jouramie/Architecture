package ca.ulaval.glo4003.authentication;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class UserIT {

  private static final String SOME_USERNAME = "name";

  private static final UserRole SOME_ROLE = UserRole.ADMINISTRATOR;

  private static final UserCreationDto A_CREATION_REQUEST =
      new UserCreationDto(SOME_USERNAME, "password", SOME_ROLE);

  private static final UserCreationDto AN_INVALID_CREATION_REQUEST =
      new UserCreationDto("", "", null);

  private static final String USERS_ROUTE = "/api/users";

  @Rule
  public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

  @Test
  public void whenCreatingUser_thenReturnCreatedUserInformation() {
    //@formatter:off
    given()
        .body(A_CREATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(USERS_ROUTE)
    .then()
        .statusCode(CREATED.getStatusCode())
        .body("username", equalTo(SOME_USERNAME))
        .body("role", equalTo(SOME_ROLE.toString()));
    //@formatter:on
  }

  @Test
  public void givenAlreadyUsedUserName_whenCreatingUser_thenBadRequest() {
    given().body(A_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON).post(USERS_ROUTE);

    //@formatter:off
    given()
        .body(A_CREATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(USERS_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode());
    //@formatter:on
  }

  @Test
  public void givenInvalidInputs_whenCreatingUser_thenBadRequest() {
    //@formatter:off
    given()
        .body(AN_INVALID_CREATION_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
    .when()
        .post(USERS_ROUTE)
    .then()
        .statusCode(BAD_REQUEST.getStatusCode())
        .body("inputErrors", any(List.class));
    //@formatter:on
  }
}

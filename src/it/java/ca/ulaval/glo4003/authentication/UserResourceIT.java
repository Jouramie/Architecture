package ca.ulaval.glo4003.authentication;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static org.hamcrest.Matchers.any;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import ca.ulaval.glo4003.ws.api.authentication.UserDto;
import ca.ulaval.glo4003.ws.api.authentication.UserRole;
import javax.ws.rs.core.MediaType;
import org.junit.Rule;
import org.junit.Test;

public class UserResourceIT {

  private static final UserCreationDto A_CREATION_REQUEST =
      new UserCreationDto("name", "password", UserRole.ADMINISTRATOR);

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
        .body(any(UserDto.class));
    //@formatter:on
  }

  @Test
  public void givenAlreadyUsedUserName_whenCreatingUser_thenBadRequest() {
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
}

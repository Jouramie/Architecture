package ca.ulaval.glo4003.authentication;

import ca.ulaval.glo4003.ResetServerBetweenTest;
import ca.ulaval.glo4003.ws.api.authentication.UserCreationDto;
import ca.ulaval.glo4003.ws.api.authentication.UserDto;
import ca.ulaval.glo4003.ws.domain.user.UserRole;

import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.any;

public class UserResourceIT {

    private static final UserCreationDto A_CREATION_REQUEST =
        new UserCreationDto("name", "password", UserRole.ADMINISTRATOR);

    private static final String USERS_ROUTE = "/api/users";

    @Rule
    public ResetServerBetweenTest resetServerBetweenTest = new ResetServerBetweenTest();

    @Test
    public void whenCreatingUser_thenReturnCreatedUserInformation() {
        given().body(A_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON)
            .post(USERS_ROUTE)
            .then()
            .statusCode(CREATED.getStatusCode())
            .body("username", equalTo(A_CREATION_REQUEST.username))
            .body("role", equalTo(A_CREATION_REQUEST.role.toString()));
    }

    @Test
    public void givenAlreadyUsedUserName_whenCreatingUser_thenBadRequest() {
        given().body(A_CREATION_REQUEST).contentType(MediaType.APPLICATION_JSON)
            .post(USERS_ROUTE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());
    }
}

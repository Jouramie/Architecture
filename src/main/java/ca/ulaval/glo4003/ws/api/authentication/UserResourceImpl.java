package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.ws.application.user.UserCreationService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;


public class UserResourceImpl implements UserResource {

    private UserCreationService userCreationService;

    @Inject
    public UserResourceImpl(UserCreationService userCreationService) {
        this.userCreationService = userCreationService;
    }

    @Override
    public Response createUser(UserCreationDto userCreationDto) {
        UserDto createdUser = userCreationService.createUser(userCreationDto);
        return Response.status(CREATED).entity(createdUser).build();
    }
}

package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.ws.application.user.UserCreationService;

import javax.inject.Inject;

public class UserResourceImpl implements UserResource {

    private UserCreationService userCreationService;

    @Inject
    public UserResourceImpl(UserCreationService userCreationService) {
        this.userCreationService = userCreationService;
    }

    @Override
    public UserDto createUser(UserCreationDto userCreationDto) {
        return userCreationService.createUser(userCreationDto);
    }
}

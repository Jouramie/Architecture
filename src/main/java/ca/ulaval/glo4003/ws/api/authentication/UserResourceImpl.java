package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.service.authentication.UserCreationService;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Resource
public class UserResourceImpl implements UserResource {

  private final UserCreationService userCreationService;

  private final RequestValidator requestValidator;

  @Inject
  public UserResourceImpl(UserCreationService userCreationService,
                          RequestValidator requestValidator) {
    this.userCreationService = userCreationService;
    this.requestValidator = requestValidator;
  }

  @Override
  public Response createUser(UserCreationDto userCreationDto) {
    requestValidator.validate(userCreationDto);
    UserDto createdUser = userCreationService.createInvestorUser(userCreationDto);
    return Response.status(CREATED).entity(createdUser).build();
  }
}

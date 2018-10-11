package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.OK;

import ca.ulaval.glo4003.domain.user.UserNotFoundException;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Resource
public class AuthenticationResourceImpl implements AuthenticationResource {

  private final AuthenticationService authenticationService;
  private final RequestValidator requestValidator;

  @Inject
  public AuthenticationResourceImpl(AuthenticationService authenticationService,
                                    RequestValidator requestValidator) {
    this.authenticationService = authenticationService;
    this.requestValidator = requestValidator;
  }

  @Override
  public Response authenticate(AuthenticationRequestDto authenticationRequest) {
    requestValidator.validate(authenticationRequest);
    AuthenticationResponseDto authenticationResponse
        = authenticationService.authenticate(authenticationRequest);
    return Response.status(OK).entity(authenticationResponse).build();
  }
}

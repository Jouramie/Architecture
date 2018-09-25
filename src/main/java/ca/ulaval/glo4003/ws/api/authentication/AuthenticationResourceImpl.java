package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.CREATED;

import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationService;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Resource
public class AuthenticationResourceImpl implements AuthenticationResource {

  private final AuthenticationService authenticationService;

  @Inject
  public AuthenticationResourceImpl(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public Response authenticate(AuthenticationRequestDto authenticationRequest) {
    AuthenticationResponseDto authenticationResponse
        = authenticationService.authenticate(authenticationRequest);
    return Response.status(CREATED).entity(authenticationResponse).build();
  }
}

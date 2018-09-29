package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.OK;

import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationService;
import javax.ws.rs.core.Response;

public class TokenRevocationResourceImpl implements TokenRevocationResource {

  private final AuthenticationService authenticationService;

  public TokenRevocationResourceImpl(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public Response revokeToken() {
    authenticationService.revokeToken();
    return Response.status(OK).build();
  }
}

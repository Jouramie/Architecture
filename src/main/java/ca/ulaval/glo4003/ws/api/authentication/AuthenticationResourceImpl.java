package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.OK;

import ca.ulaval.glo4003.service.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Resource
public class AuthenticationResourceImpl implements AuthenticationResource {

  private final AuthenticationService authenticationService;
  private final RequestValidator requestValidator;
  private final ApiAuthenticationResponseAssembler apiAuthenticationResponseAssembler;

  @Inject
  public AuthenticationResourceImpl(AuthenticationService authenticationService,
                                    RequestValidator requestValidator,
                                    ApiAuthenticationResponseAssembler apiAuthenticationResponseAssembler) {
    this.authenticationService = authenticationService;
    this.requestValidator = requestValidator;
    this.apiAuthenticationResponseAssembler = apiAuthenticationResponseAssembler;
  }

  @Override
  public Response authenticate(AuthenticationRequestDto authenticationRequest) {
    requestValidator.validate(authenticationRequest);
    ApiAuthenticationResponseDto apiAuthenticationResponse
        = authenticationService.authenticate(authenticationRequest);
    return Response.status(ACCEPTED).entity(authenticationResponse).build();
  }

  @Override
  public Response logout() {
    authenticationService.revokeToken();
    return Response.status(OK).build();
  }
}

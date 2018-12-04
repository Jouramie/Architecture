package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.OK;

import ca.ulaval.glo4003.service.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.ws.api.authentication.assemblers.ApiAuthenticationResponseAssembler;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationResponseDto;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Resource
public class AuthenticationResourceImpl implements AuthenticationResource {

  private final AuthenticationService authenticationService;
  private final ApiAuthenticationResponseAssembler apiAuthenticationResponseAssembler;

  @Inject
  public AuthenticationResourceImpl(AuthenticationService authenticationService,
                                    ApiAuthenticationResponseAssembler apiAuthenticationResponseAssembler) {
    this.authenticationService = authenticationService;
    this.apiAuthenticationResponseAssembler = apiAuthenticationResponseAssembler;
  }

  @Override
  public Response authenticate(ApiAuthenticationRequestDto authenticationRequest) {
    AuthenticationResponseDto authenticationResponse = authenticationService.authenticate(authenticationRequest);
    ApiAuthenticationResponseDto apiAuthenticationResponseDto = apiAuthenticationResponseAssembler.toDto(authenticationResponse);
    return Response.status(ACCEPTED).entity(apiAuthenticationResponseDto).build();
  }

  @Override
  public Response logout() {
    authenticationService.revokeToken();
    return Response.status(OK).build();
  }
}

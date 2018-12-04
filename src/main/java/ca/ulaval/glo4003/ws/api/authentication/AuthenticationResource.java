package ca.ulaval.glo4003.ws.api.authentication;

import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.OK;

import ca.ulaval.glo4003.service.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.ws.api.authentication.assemblers.ApiAuthenticationResponseAssembler;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationResponseDto;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import ca.ulaval.glo4003.ws.http.authentication.AuthenticationRequiredBinding;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Resource
public class AuthenticationResource implements DocumentedAuthenticationResource {

  private final AuthenticationService authenticationService;
  private final RequestValidator requestValidator;
  private final ApiAuthenticationResponseAssembler apiAuthenticationResponseAssembler;

  @Inject
  public AuthenticationResource(AuthenticationService authenticationService,
                                RequestValidator requestValidator,
                                ApiAuthenticationResponseAssembler apiAuthenticationResponseAssembler) {
    this.authenticationService = authenticationService;
    this.requestValidator = requestValidator;
    this.apiAuthenticationResponseAssembler = apiAuthenticationResponseAssembler;
  }

  @POST
  @Path("/authenticate")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Override
  public Response authenticate(ApiAuthenticationRequestDto authenticationRequest) {
    requestValidator.validate(authenticationRequest);
    AuthenticationResponseDto authenticationResponse = authenticationService.authenticate(authenticationRequest);
    ApiAuthenticationResponseDto apiAuthenticationResponseDto = apiAuthenticationResponseAssembler.toDto(authenticationResponse);
    return Response.status(ACCEPTED).entity(apiAuthenticationResponseDto).build();
  }

  @POST()
  @Path("/logout")
  @AuthenticationRequiredBinding
  @Override
  public Response logout() {
    authenticationService.revokeToken();
    return Response.status(OK).build();
  }
}

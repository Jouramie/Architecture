package ca.ulaval.glo4003.ws.http;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import ca.ulaval.glo4003.ws.application.user.authentication.AuthenticationService;
import ca.ulaval.glo4003.ws.application.user.authentication.InvalidTokenException;
import ca.ulaval.glo4003.ws.domain.user.authentication.NoTokenFoundException;
import ca.ulaval.glo4003.ws.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.ws.infrastructure.injection.ServiceLocator;
import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@FilterRegistration
@Priority(Priorities.AUTHORIZATION)
@AuthenticationRequiredBinding
public class AuthenticationFilter implements ContainerRequestFilter {

  private final AuthenticationService authenticationService;

  public AuthenticationFilter() {
    authenticationService = ServiceLocator.INSTANCE.get(AuthenticationService.class);
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {

    AuthenticationTokenDto authenticationTokenDto
        = extractAuthenticationInfo(containerRequestContext.getHeaders());
    try {
      authenticationService.validateAuthentication(authenticationTokenDto);
    } catch (InvalidTokenException | NoTokenFoundException | NullPointerException e) {
      containerRequestContext.abortWith(Response.status(UNAUTHORIZED).build());
    }
  }

  private AuthenticationTokenDto extractAuthenticationInfo(MultivaluedMap<String, String> headers) {
    String token = headers.getFirst("token");
    String username = headers.getFirst("username");
    return new AuthenticationTokenDto(username, token);
  }
}

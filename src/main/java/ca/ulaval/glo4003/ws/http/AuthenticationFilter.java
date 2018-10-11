package ca.ulaval.glo4003.ws.http;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import ca.ulaval.glo4003.domain.user.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.authentication.TokenNotFoundException;
import ca.ulaval.glo4003.infrastructure.injection.FilterRegistration;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.service.authentication.InvalidTokenException;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import java.util.Optional;
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
  public void filter(ContainerRequestContext containerRequestContext) {
    try {
      AuthenticationTokenDto authenticationTokenDto
          = extractAuthenticationInfo(containerRequestContext.getHeaders());
      authenticationService.validateAuthentication(authenticationTokenDto);
    } catch (InvalidTokenException | IllegalArgumentException exception) {
      containerRequestContext.abortWith(Response.status(UNAUTHORIZED).build());
    }
  }

  private AuthenticationTokenDto extractAuthenticationInfo(MultivaluedMap<String, String> headers) {
    String token = Optional.ofNullable(headers.getFirst("token"))
        .orElseThrow(InvalidTokenException::new);
    return new AuthenticationTokenDto(token);
  }
}

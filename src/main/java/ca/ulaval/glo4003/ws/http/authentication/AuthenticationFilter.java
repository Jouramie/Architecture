package ca.ulaval.glo4003.ws.http.authentication;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.service.authentication.InvalidTokenException;
import ca.ulaval.glo4003.service.authentication.UnauthorizedRoleException;
import ca.ulaval.glo4003.ws.api.authentication.dto.AuthenticationTokenDto;
import ca.ulaval.glo4003.ws.http.FilterRegistration;
import java.util.List;
import java.util.Optional;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@FilterRegistration
@Priority(Priorities.AUTHORIZATION)
@AuthenticationRequiredBinding
public class AuthenticationFilter implements ContainerRequestFilter {

  private final AuthenticationService authenticationService;
  private final AcceptedRoleReflectionExtractor acceptedRolesReflectionExtractor;

  @Context
  private ResourceInfo resourceInfo;

  public AuthenticationFilter() {
    authenticationService = ServiceLocator.INSTANCE.get(AuthenticationService.class);
    acceptedRolesReflectionExtractor = ServiceLocator.INSTANCE.get(AcceptedRoleReflectionExtractor.class);
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    try {
      AuthenticationTokenDto authenticationTokenDto = extractAuthenticationInfo(containerRequestContext.getHeaders());
      List<UserRole> acceptedRoles = acceptedRolesReflectionExtractor.extractAcceptedRoles(resourceInfo);

      authenticationService.validateAuthentication(authenticationTokenDto, acceptedRoles);
    } catch (InvalidTokenException | UnauthorizedRoleException | IllegalArgumentException exception) {
      containerRequestContext.abortWith(Response.status(UNAUTHORIZED).build());
    }
  }

  private AuthenticationTokenDto extractAuthenticationInfo(MultivaluedMap<String, String> headers) {
    String token = Optional.ofNullable(headers.getFirst("token"))
        .orElseThrow(InvalidTokenException::new);
    return new AuthenticationTokenDto(token);
  }
}

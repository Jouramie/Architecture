package ca.ulaval.glo4003.ws.http;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.infrastructure.injection.ServiceLocator;
import ca.ulaval.glo4003.service.authentication.AuthenticationService;
import ca.ulaval.glo4003.service.authentication.InvalidTokenException;
import ca.ulaval.glo4003.ws.api.authentication.dto.AuthenticationTokenDto;
import java.lang.reflect.Method;
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
  @Context
  private ResourceInfo resourceInfo;

  public AuthenticationFilter() {
    authenticationService = ServiceLocator.INSTANCE.get(AuthenticationService.class);
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    UserRole requiredRole = extractRequiredRole();

    try {
      AuthenticationTokenDto authenticationTokenDto = extractAuthenticationInfo(containerRequestContext.getHeaders());
      authenticationService.validateAuthentication(authenticationTokenDto, requiredRole);
    } catch (InvalidTokenException | IllegalArgumentException exception) {
      containerRequestContext.abortWith(Response.status(UNAUTHORIZED).build());
    }
  }

  // TODO might be useful to extract this in a class to mock it
  private UserRole extractRequiredRole() {
    AuthenticationRequiredBinding binding = null;

    Method method = resourceInfo.getResourceMethod();
    if (method != null) {
      binding = method.getAnnotation(AuthenticationRequiredBinding.class);
    }

    if (binding == null) {
      Class<?> clazz = resourceInfo.getResourceClass();
      if (clazz != null) {
        binding = clazz.getAnnotation(AuthenticationRequiredBinding.class);
      }
    }

    if (binding != null) {
      return binding.requiredRole();
    }

    throw new RuntimeException("Binding annotations should be added to implementation and not interface.");
  }

  private AuthenticationTokenDto extractAuthenticationInfo(MultivaluedMap<String, String> headers) {
    String token = Optional.ofNullable(headers.getFirst("token"))
        .orElseThrow(InvalidTokenException::new);
    return new AuthenticationTokenDto(token);
  }
}

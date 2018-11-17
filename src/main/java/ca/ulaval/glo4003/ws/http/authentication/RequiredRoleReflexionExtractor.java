package ca.ulaval.glo4003.ws.http.authentication;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.UserRole;
import java.lang.reflect.Method;
import javax.ws.rs.container.ResourceInfo;

@Component
public class RequiredRoleReflexionExtractor {

  public UserRole extractRequiredRole(ResourceInfo resourceInfo) {
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

    throw new RuntimeException("Please use 'AuthenticationRequiredBinding' annotations on implementations and not "
        + "interfaces.");
  }
}

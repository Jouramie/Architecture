package ca.ulaval.glo4003.ws.http.authentication;

import ca.ulaval.glo4003.domain.user.UserRole;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.ws.rs.NameBinding;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationRequiredBinding {
  UserRole requiredRole() default UserRole.INVESTOR;
}

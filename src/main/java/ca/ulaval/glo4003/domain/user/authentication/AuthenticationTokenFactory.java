package ca.ulaval.glo4003.domain.user.authentication;

import ca.ulaval.glo4003.infrastructure.injection.Component;
import java.util.UUID;

@Component
public class AuthenticationTokenFactory {

  public AuthenticationToken createToken(String username) {
    return new AuthenticationToken(UUID.randomUUID().toString(), username);
  }
}

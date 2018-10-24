package ca.ulaval.glo4003.domain.user.authentication;

import ca.ulaval.glo4003.service.Component;
import java.util.UUID;

@Component
public class AuthenticationTokenFactory {

  public AuthenticationToken createToken(String email) {
    return new AuthenticationToken(UUID.randomUUID().toString(), email);
  }
}

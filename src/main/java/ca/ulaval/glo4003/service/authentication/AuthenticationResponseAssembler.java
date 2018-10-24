package ca.ulaval.glo4003.service.authentication;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.infrastructure.injection.Component;

@Component
public class AuthenticationResponseAssembler {

  public AuthenticationResponseDto toDto(AuthenticationToken token) {
    return new AuthenticationResponseDto(token.token);
  }
}

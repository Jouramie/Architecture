package ca.ulaval.glo4003.service.authentication;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.authentication.AuthenticationToken;

@Component
public class AuthenticationResponseAssembler {

  public AuthenticationResponseDto toDto(AuthenticationToken token) {
    return new AuthenticationResponseDto(token.token);
  }
}

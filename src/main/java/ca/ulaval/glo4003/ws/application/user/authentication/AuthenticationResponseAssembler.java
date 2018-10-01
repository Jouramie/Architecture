package ca.ulaval.glo4003.ws.application.user.authentication;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.infrastructure.injection.Component;

@Component
public class AuthenticationResponseAssembler {

  public AuthenticationResponseDto toDto(AuthenticationToken token) {
    return new AuthenticationResponseDto(token.token);
  }
}

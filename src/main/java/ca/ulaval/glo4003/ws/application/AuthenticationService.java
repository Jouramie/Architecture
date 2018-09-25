package ca.ulaval.glo4003.ws.application;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationRequestDto;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.ws.infrastructure.injection.Component;

@Component
public class AuthenticationService {
  public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest) {
    return null;
  }
}

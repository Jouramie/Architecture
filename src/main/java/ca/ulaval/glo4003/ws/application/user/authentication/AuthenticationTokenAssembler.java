package ca.ulaval.glo4003.ws.application.user.authentication;

import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.infrastructure.injection.Component;

@Component
public class AuthenticationTokenAssembler {

  public AuthenticationToken toModel(AuthenticationTokenDto authenticationTokenDto) {
    return new AuthenticationToken(authenticationTokenDto.token, authenticationTokenDto.username);
  }
}

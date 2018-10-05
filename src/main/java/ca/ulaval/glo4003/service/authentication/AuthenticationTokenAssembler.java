package ca.ulaval.glo4003.service.authentication;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.api.authentication.AuthenticationTokenDto;

@Component
public class AuthenticationTokenAssembler {

  public AuthenticationToken toModel(AuthenticationTokenDto authenticationTokenDto) {
    return new AuthenticationToken(authenticationTokenDto.token, authenticationTokenDto.email);
  }
}

package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.authentication.AuthenticationResponseDto;

@Component
public class ApiAuthenticationResponseAssembler {

  public ApiAuthenticationResponseDto toDto(AuthenticationResponseDto authenticationResponseDto) {
    return new ApiAuthenticationResponseDto(authenticationResponseDto.token);
  }
}

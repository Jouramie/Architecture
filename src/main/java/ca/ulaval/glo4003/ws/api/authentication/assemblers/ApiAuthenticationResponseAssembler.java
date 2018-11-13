package ca.ulaval.glo4003.ws.api.authentication.assemblers;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.authentication.AuthenticationResponseDto;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiAuthenticationResponseDto;

@Component
public class ApiAuthenticationResponseAssembler {

  public ApiAuthenticationResponseDto toDto(AuthenticationResponseDto authenticationResponseDto) {
    return new ApiAuthenticationResponseDto(authenticationResponseDto.token);
  }
}

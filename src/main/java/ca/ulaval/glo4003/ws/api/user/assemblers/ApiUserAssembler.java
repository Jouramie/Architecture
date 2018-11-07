package ca.ulaval.glo4003.ws.api.user.assemblers;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.authentication.UserDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;

@Component
public class ApiUserAssembler {
  public ApiUserDto toDto(UserDto userDto) {
    return new ApiUserDto(userDto.email, userDto.role, null);
  }
}

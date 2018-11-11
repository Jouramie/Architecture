package ca.ulaval.glo4003.ws.api.authentication.assemblers;

import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.service.authentication.UserDto;
import ca.ulaval.glo4003.ws.api.authentication.dto.ApiUserDto;

@Component
public class ApiUserAssembler {
  public ApiUserDto toDto(UserDto userDto) {
    return new ApiUserDto(userDto.email, userDto.role);
  }
}

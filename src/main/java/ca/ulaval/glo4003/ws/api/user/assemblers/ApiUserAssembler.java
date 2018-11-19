package ca.ulaval.glo4003.ws.api.user.assemblers;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.user.UserDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import java.util.List;

@Component
public class ApiUserAssembler {
  public ApiUserDto toDto(UserDto userDto) {
    return new ApiUserDto(userDto.email, userDto.role, null);
  }

  public List<ApiUserDto> toDto(List<UserDto> users) {
    return users.stream().map(this::toDto).collect(toList());
  }
}

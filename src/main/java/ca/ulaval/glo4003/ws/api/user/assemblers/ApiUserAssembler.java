package ca.ulaval.glo4003.ws.api.user.assemblers;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.user.UserDto;
import ca.ulaval.glo4003.ws.api.user.dto.ApiUserDto;
import java.util.List;
import javax.inject.Inject;

@Component
public class ApiUserAssembler {

  private final ApiLimitAssembler limitAssembler;

  @Inject
  public ApiUserAssembler(ApiLimitAssembler limitAssembler) {
    this.limitAssembler = limitAssembler;
  }

  public ApiUserDto toDto(UserDto userDto) {
    return new ApiUserDto(userDto.email, userDto.role, limitAssembler.toDto(userDto.limit));
  }

  public List<ApiUserDto> toDtoList(List<UserDto> users) {
    return users.stream().map(this::toDto).collect(toList());
  }
}

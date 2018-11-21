package ca.ulaval.glo4003.service.user;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.Investor;
import java.util.List;


@Component
public class UserAssembler {
  public UserDto toDto(Investor user) {
    return new UserDto(user.getEmail(), user.getRole());
  }

  public List<UserDto> toDtoList(List<Investor> users) {
    return users.stream().map(this::toDto).collect(toList());
  }
}

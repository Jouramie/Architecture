package ca.ulaval.glo4003.service.authentication;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.User;
import java.util.List;


@Component
public class UserAssembler {
  public UserDto toDto(User user) {
    return new UserDto(user.getEmail(), user.getRole());
  }

  public List<UserDto> toDto(List<User> users) {
    return users.stream().map(this::toDto).collect(toList());
  }
}

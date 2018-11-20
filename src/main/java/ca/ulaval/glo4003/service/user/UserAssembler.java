package ca.ulaval.glo4003.service.user;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.service.user.limit.LimitAssembler;
import java.util.List;

@Component
public class UserAssembler {
  private final LimitAssembler limitAssembler;

  public UserAssembler(LimitAssembler limitAssembler) {
    this.limitAssembler = limitAssembler;
  }

  public UserDto toDto(User user) {
    return new UserDto(user.getEmail(), user.getRole(), limitAssembler.toDto(user.getLimit()));
  }

  public List<UserDto> toDtoList(List<User> users) {
    return users.stream().map(this::toDto).collect(toList());
  }
}

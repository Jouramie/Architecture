package ca.ulaval.glo4003.service.user;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.user.Administrator;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.service.user.limit.LimitAssembler;
import ca.ulaval.glo4003.service.user.limit.LimitDto;
import java.util.List;
import javax.inject.Inject;

@Component
public class UserAssembler {
  private final LimitAssembler limitAssembler;

  @Inject
  public UserAssembler(LimitAssembler limitAssembler) {
    this.limitAssembler = limitAssembler;
  }

  public UserDto toDto(User user) {
    LimitDto limit = getLimit(user);

    return new UserDto(user.getEmail(), user.getRole(), limit);
  }

  private LimitDto getLimit(User user) {
    if (user instanceof Administrator) {
      return null;
    }

    if (user instanceof Investor) {
      return limitAssembler.toDto(((Investor) user).getLimit());
    }

    throw new UnsupportedOperationException("There is no conversion for this user: " + user);
  }

  public List<UserDto> toDtoList(List<User> users) {
    return users.stream().map(this::toDto).collect(toList());
  }
}

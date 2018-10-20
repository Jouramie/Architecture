package ca.ulaval.glo4003.service.authentication;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.service.Component;
import ca.ulaval.glo4003.ws.api.authentication.UserDto;

@Component
public class UserAssembler {

  public UserDto toDto(User user) {
    return new UserDto(user.getEmail(), user.getRole());
  }
}

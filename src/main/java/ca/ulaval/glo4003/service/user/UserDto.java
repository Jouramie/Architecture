package ca.ulaval.glo4003.service.user;

import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.service.user.limit.LimitDto;

public class UserDto {
  public final String email;
  public final UserRole role;
  public final LimitDto limit;

  public UserDto(String email, UserRole role, LimitDto limit) {
    this.email = email;
    this.role = role;
    this.limit = limit;
  }
}

package ca.ulaval.glo4003.service.authentication;

import ca.ulaval.glo4003.domain.user.UserRole;

public class UserDto {
  public final String email;
  public final UserRole role;

  public UserDto(String email, UserRole role) {
    this.email = email;
    this.role = role;
  }
}

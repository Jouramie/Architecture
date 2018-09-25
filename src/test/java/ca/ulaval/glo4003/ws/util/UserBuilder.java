package ca.ulaval.glo4003.ws.util;

import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserRole;

public class UserBuilder {
  private static final String DEFAULT_USERNAME = "username";
  private static final String DEFAULT_PASSWORD = "password";
  private static final UserRole DEFAULT_USER_ROLE = UserRole.INVESTOR;

  private final String username = DEFAULT_USERNAME;
  private final String password = DEFAULT_PASSWORD;
  private final UserRole userRole = DEFAULT_USER_ROLE;

  public User build() {
    return new User(username, password, userRole);
  }
}

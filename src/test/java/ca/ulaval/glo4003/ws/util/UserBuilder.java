package ca.ulaval.glo4003.ws.util;

import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserRole;

public class UserBuilder {
  public static final String DEFAULT_USERNAME = "username";
  public static final String DEFAULT_PASSWORD = "password";
  public static final UserRole DEFAULT_USER_ROLE = UserRole.INVESTOR;

  private String username = DEFAULT_USERNAME;
  private String password = DEFAULT_PASSWORD;
  private UserRole userRole = DEFAULT_USER_ROLE;

  public User build() {
    return new User(username, password, userRole);
  }
}

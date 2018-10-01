package ca.ulaval.glo4003.ws.util;

import ca.ulaval.glo4003.ws.domain.user.User;
import ca.ulaval.glo4003.ws.domain.user.UserRole;

public class UserBuilder {
  public static final String DEFAULT_USERNAME = "username";
  public static final String DEFAULT_PASSWORD = "password";
  private static final UserRole DEFAULT_USER_ROLE = UserRole.INVESTOR;

  private final UserRole userRole = DEFAULT_USER_ROLE;
  private String username = DEFAULT_USERNAME;
  private String password = DEFAULT_PASSWORD;

  public UserBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public UserBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public User build() {
    return new User(username, password, userRole);
  }

  public User buildDefault() {
    return new User(DEFAULT_USERNAME, DEFAULT_PASSWORD, DEFAULT_USER_ROLE);
  }
}

package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.LimitBuilder;

public class UserBuilder {
  public static final String DEFAULT_EMAIL = "email";
  public static final String DEFAULT_PASSWORD = "password";
  private static final UserRole DEFAULT_USER_ROLE = UserRole.INVESTOR;
  private static final Limit DEFAULT_LIMIT = new LimitBuilder().build();

  private UserRole userRole = DEFAULT_USER_ROLE;
  private String email = DEFAULT_EMAIL;
  private String password = DEFAULT_PASSWORD;
  private Limit limit = DEFAULT_LIMIT;

  public UserBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public UserBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public UserBuilder withRole(UserRole role) {
    userRole = role;
    return this;
  }

  public UserBuilder withLimit(Limit limit) {
    this.limit = limit;
    return this;
  }

  public User build() {
    return new User(email, password, userRole, new Cart(), new Portfolio(), limit);
  }
}

package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.user.Administrator;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.NullLimit;

public class UserBuilder {
  public static final String DEFAULT_EMAIL = "email";
  public static final String DEFAULT_PASSWORD = "password";
  private static final Limit DEFAULT_LIMIT = new NullLimit();

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

  public UserBuilder withLimit(Limit limit) {
    this.limit = limit;
    return this;
  }

  public User build() {
    return buildAdministrator();
  }

  public Investor buildInvestor() {
    return new Investor(email, password, new Cart(), new Portfolio(), limit);
  }

  public Administrator buildAdministrator() {
    return new Administrator(email, password);
  }
}

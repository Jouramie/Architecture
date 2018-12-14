package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.user.limit.Limit;
import ca.ulaval.glo4003.domain.user.limit.LimitBuilder;

public class UserBuilder {
  public static final String DEFAULT_EMAIL = "email";
  public static final String DEFAULT_PASSWORD = "password";
  private static final Limit DEFAULT_LIMIT = new LimitBuilder().build();

  private String email = DEFAULT_EMAIL;
  private String password = DEFAULT_PASSWORD;
  private Limit limit = DEFAULT_LIMIT;
  private Cart cart = new Cart();
  private Portfolio portfolio = new Portfolio();

  public UserBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public UserBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public UserBuilder withCart(Cart cart) {
    this.cart = cart;
    return this;
  }

  public UserBuilder withPortfolio(Portfolio portfolio) {
    this.portfolio = portfolio;
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
    return new Investor(email, password, cart, portfolio, limit);
  }

  public Administrator buildAdministrator() {
    return new Administrator(email, password);
  }
}

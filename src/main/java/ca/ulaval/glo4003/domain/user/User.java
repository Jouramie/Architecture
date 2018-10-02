package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.cart.Cart;

public class User {
  private final String username;
  private final String password;
  private final UserRole role;
  private final Cart cart;

  public User(String username, String password, UserRole role) {
    this.username = username;
    this.password = password;
    this.role = role;
    cart = new Cart();
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public UserRole getRole() {
    return role;
  }

  public boolean isThisYourPassword(String password) {
    return this.password.equals(password);
  }

  public Cart getCart() {
    return cart;
  }
}

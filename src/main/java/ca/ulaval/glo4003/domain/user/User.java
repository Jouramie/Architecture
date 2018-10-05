package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.cart.Cart;

public class User {
  private final String email;
  private final String password;
  private final UserRole role;
  private final Cart cart;

  public User(String email, String password, UserRole role) {
    this.email = email;
    this.password = password;
    this.role = role;
    cart = new Cart();
  }

  public String getEmail() {
    return email;
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

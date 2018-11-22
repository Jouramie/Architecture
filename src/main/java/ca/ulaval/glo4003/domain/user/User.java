package ca.ulaval.glo4003.domain.user;

import java.util.List;

public abstract class User {
  protected final String email;
  private final String password;

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public boolean isThisYourPassword(String password) {
    return this.password.equals(password);
  }

  public abstract UserRole getRole();

  public abstract boolean haveRoleIn(List<UserRole> roles);
}

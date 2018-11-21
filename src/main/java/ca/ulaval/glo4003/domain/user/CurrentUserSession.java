package ca.ulaval.glo4003.domain.user;

public class CurrentUserSession {

  private final ThreadLocal<Investor> currentUser = new ThreadLocal<>();

  public Investor getCurrentUser() {
    return currentUser.get();
  }

  public void setCurrentUser(Investor user) {
    currentUser.set(user);
  }
}

package ca.ulaval.glo4003.domain.user;

public class CurrentUserSession {

  private final ThreadLocal<User> currentUser = new ThreadLocal<>();

  public <T> T getCurrentUser(Class<T> clazz) {
    return clazz.cast(getCurrentUser());
  }

  public User getCurrentUser() {
    return currentUser.get();
  }

  public void setCurrentUser(User user) {
    currentUser.set(user);
  }
}

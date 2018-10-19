package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.infrastructure.injection.Component;

@Component
public class CurrentUserSession {

  private final ThreadLocal<User> currentUser = new ThreadLocal<>();

  public User getCurrentUser() {
    return currentUser.get();
  }

  public void setCurrentUser(User user) {
    currentUser.set(user);
  }
}

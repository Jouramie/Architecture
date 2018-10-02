package ca.ulaval.glo4003.infrastructure.persistence;

import ca.ulaval.glo4003.domain.user.CurrentUserRepository;
import ca.ulaval.glo4003.domain.user.User;

public class InMemoryCurrentUserRepository implements CurrentUserRepository {

  private final ThreadLocal<User> currentUser = new ThreadLocal<>();

  @Override
  public User getCurrentUser() {
    return currentUser.get();
  }

  @Override
  public void setCurrentUser(User user) {
    currentUser.set(user);
  }
}

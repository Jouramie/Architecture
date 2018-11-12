package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.Component;

@Component
public class UserFactory {
  public User create(String email, String password, UserRole role) {
    return new User(email, password, role);
  }
}

package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.service.Component;

@Component
public class UserFactory {
  public User createInvestor(String email, String password) {
    return new User(email, password, UserRole.INVESTOR);
  }

  public User createAdministrator(String email, String password) {
    return new User(email, password, UserRole.ADMINISTRATOR);
  }
}

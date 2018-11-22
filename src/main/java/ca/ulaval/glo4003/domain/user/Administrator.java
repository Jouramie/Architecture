package ca.ulaval.glo4003.domain.user;

import java.util.List;

public class Administrator extends User {
  public Administrator(String email, String password) {
    super(email, password);
  }

  @Override
  public UserRole getRole() {
    return UserRole.ADMINISTRATOR;
  }

  @Override
  public boolean haveRoleIn(List<UserRole> roles) {
    return roles.contains(UserRole.ADMINISTRATOR);
  }
}

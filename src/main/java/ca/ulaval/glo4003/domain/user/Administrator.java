package ca.ulaval.glo4003.domain.user;

public class Administrator extends User {
  public Administrator(String email, String password) {
    super(email, password);
  }

  @Override
  public UserRole getRole() {
    return UserRole.ADMINISTRATOR;
  }
}

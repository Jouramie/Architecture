package ca.ulaval.glo4003.domain.user;

public interface CurrentUserSession {

  User getCurrentUser();

  void setCurrentUser(User user);
}

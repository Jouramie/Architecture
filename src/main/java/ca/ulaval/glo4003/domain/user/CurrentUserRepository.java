package ca.ulaval.glo4003.domain.user;

public interface CurrentUserRepository {

  User getCurrentUser();

  void setCurrentUser(User user);
}

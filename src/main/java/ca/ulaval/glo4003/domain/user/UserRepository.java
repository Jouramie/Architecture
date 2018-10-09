package ca.ulaval.glo4003.domain.user;

public interface UserRepository {
  void add(User user) throws UserAlreadyExistsException;

  void update(User user) throws UserNotFoundException;

  User find(String email) throws UserNotFoundException;
}

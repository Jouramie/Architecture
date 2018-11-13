package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;

public interface UserRepository {
  void add(User user) throws UserAlreadyExistsException;

  void update(User user) throws UserNotFoundException;

  User find(String email) throws UserNotFoundException;
}

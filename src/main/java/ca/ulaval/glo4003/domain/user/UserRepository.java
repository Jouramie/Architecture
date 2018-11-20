package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import java.util.List;

public interface UserRepository {
  void add(User user) throws UserAlreadyExistsException;

  void update(User user) throws UserNotFoundException;

  User find(String email) throws UserNotFoundException;

  List<User> findAll();
}

package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.exceptions.WrongRoleException;
import java.util.List;

public interface UserRepository {
  void add(User user) throws UserAlreadyExistsException;

  void update(User user) throws UserNotFoundException;

  User find(String email) throws UserNotFoundException;

  <T extends User> T find(String email, Class<T> clazz) throws UserNotFoundException, WrongRoleException;

  List<User> findAll();
}

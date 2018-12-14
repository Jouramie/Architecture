package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.user.exception.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.exception.WrongRoleException;
import java.util.List;

public interface UserRepository {
  void add(User user) throws UserAlreadyExistsException;

  void update(User user) throws UserNotFoundException;

  User findByEmail(String email) throws UserNotFoundException;

  <T extends User> T findByEmail(String email, Class<T> clazz) throws UserNotFoundException, WrongRoleException;

  List<User> findAll();

  List<Investor> findInvestors();
}

package ca.ulaval.glo4003.domain.user;

import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import java.util.List;

public interface UserRepository {
  void add(Investor user) throws UserAlreadyExistsException;

  void update(Investor user) throws UserNotFoundException;

  Investor find(String email) throws UserNotFoundException;

  List<Investor> findAll();
}

package ca.ulaval.glo4003.infrastructure.user;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.UserRole;
import ca.ulaval.glo4003.domain.user.exception.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.exception.WrongRoleException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

  private final Map<String, User> content = new ConcurrentHashMap<>();

  @Override
  public void add(User user) throws UserAlreadyExistsException {
    if (content.containsKey(user.getEmail())) {
      throw new UserAlreadyExistsException();
    }
    content.put(user.getEmail(), user);
  }

  @Override
  public void update(User user) throws UserNotFoundException {
    if (!content.containsKey(user.getEmail())) {
      throw new UserNotFoundException();
    }
    content.put(user.getEmail(), user);
  }

  @Override
  public <T extends User> T findByEmail(String email, Class<T> clazz) throws UserNotFoundException, WrongRoleException {
    try {
      return clazz.cast(findByEmail(email));
    } catch (ClassCastException e) {
      throw new WrongRoleException(e);
    }
  }

  @Override
  public User findByEmail(String email) throws UserNotFoundException {
    return Optional.ofNullable(content.get(email)).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(content.values());
  }

  @Override
  public List<Investor> findInvestors() {
    return content.values().stream()
        .filter(user -> user.getRole().equals(UserRole.INVESTOR))
        .map(user -> (Investor) user).collect(toList());
  }
}

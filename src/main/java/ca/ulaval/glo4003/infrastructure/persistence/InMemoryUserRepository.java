package ca.ulaval.glo4003.infrastructure.persistence;

import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.UserRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

  private final Map<String, User> content = new ConcurrentHashMap<>();

  @Override
  public void save(User user) {
    if (content.containsKey(user.getUsername())) {
      throw new UserAlreadyExistsException();
    }
    content.put(user.getUsername(), user);
  }

  @Override
  public User find(String username) {
    return Optional.ofNullable(content.get(username)).orElseThrow(UserNotFoundException::new);
  }
}

package ca.ulaval.glo4003.ws.domain.user;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryInMemory implements UserRepository {

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
    return Optional.ofNullable(content.get(username)).orElseThrow(NoSuchElementException::new);
  }
}

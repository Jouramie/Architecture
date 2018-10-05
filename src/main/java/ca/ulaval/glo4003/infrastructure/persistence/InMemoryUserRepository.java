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
  public void add(User user) {
    if (content.containsKey(user.getEmail())) {
      throw new UserAlreadyExistsException();
    }
    content.put(user.getEmail(), user);
  }

  @Override
  public void update(User user) {
    if (!content.containsKey(user.getEmail())) {
      throw new UserNotFoundException();
    }
    content.put(user.getEmail(), user);
  }

  @Override
  public User find(String email) {
    return Optional.ofNullable(content.get(email)).orElseThrow(UserNotFoundException::new);
  }
}
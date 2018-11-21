package ca.ulaval.glo4003.infrastructure.persistence;

import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserAlreadyExistsException;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

  private final Map<String, Investor> content = new ConcurrentHashMap<>();

  @Override
  public void add(Investor user) throws UserAlreadyExistsException {
    if (content.containsKey(user.getEmail())) {
      throw new UserAlreadyExistsException();
    }
    content.put(user.getEmail(), user);
  }

  @Override
  public void update(Investor user) throws UserNotFoundException {
    if (!content.containsKey(user.getEmail())) {
      throw new UserNotFoundException();
    }
    content.put(user.getEmail(), user);
  }

  @Override
  public Investor find(String email) throws UserNotFoundException {
    return Optional.ofNullable(content.get(email)).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public List<Investor> findAll() {
    return new ArrayList<>(content.values());
  }
}

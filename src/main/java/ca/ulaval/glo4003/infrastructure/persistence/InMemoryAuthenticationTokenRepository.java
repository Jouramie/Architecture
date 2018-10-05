package ca.ulaval.glo4003.infrastructure.persistence;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.domain.user.authentication.NoTokenFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAuthenticationTokenRepository implements AuthenticationTokenRepository {

  private final Map<String, AuthenticationToken> tokens = new ConcurrentHashMap<>();

  @Override
  public AuthenticationToken getByUUID(UUID uuid) {
    return Optional.ofNullable(tokens.get(uuid.toString())).orElseThrow(NoTokenFoundException::new);
  }

  @Override
  public void add(AuthenticationToken token) {
    tokens.put(token.token, token);
  }

  @Override
  public void remove(String email) {
    tokens.remove(email);
  }
}

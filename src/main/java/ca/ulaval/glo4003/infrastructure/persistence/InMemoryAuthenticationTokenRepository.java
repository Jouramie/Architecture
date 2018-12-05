package ca.ulaval.glo4003.infrastructure.persistence;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.domain.user.authentication.TokenNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAuthenticationTokenRepository implements AuthenticationTokenRepository {

  private final Map<String, AuthenticationToken> tokens = new ConcurrentHashMap<>();

  @Override
  public Optional<AuthenticationToken> findByUUID(UUID uuid) {
    return Optional.ofNullable(tokens.get(uuid.toString()));
  }

  @Override
  public void add(AuthenticationToken token) {
    tokens.put(token.token, token);
  }

  @Override
  public void remove(String email) throws TokenNotFoundException {
    AuthenticationToken foundToken = tokens.values().stream()
        .filter(token -> token.email.equals(email))
        .findFirst()
        .orElseThrow(TokenNotFoundException::new);
    tokens.remove(foundToken.token);
  }
}

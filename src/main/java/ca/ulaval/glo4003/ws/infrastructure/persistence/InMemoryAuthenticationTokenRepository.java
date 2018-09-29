package ca.ulaval.glo4003.ws.infrastructure.persistence;

import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.ws.domain.user.authentication.NoTokenFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAuthenticationTokenRepository implements AuthenticationTokenRepository {

  private final Map<String, AuthenticationToken> tokens = new ConcurrentHashMap<>();

  @Override
  public AuthenticationToken getTokenForUser(String username) {
    return Optional.ofNullable(tokens.get(username)).orElseThrow(NoTokenFoundException::new);
  }

  @Override
  public void addTokenForUser(AuthenticationToken token) {
    tokens.put(token.username, token);
  }
}

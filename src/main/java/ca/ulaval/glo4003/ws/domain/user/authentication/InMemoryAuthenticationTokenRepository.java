package ca.ulaval.glo4003.ws.domain.user.authentication;

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

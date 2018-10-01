package ca.ulaval.glo4003.infrastructure.persistence;

import ca.ulaval.glo4003.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.domain.user.authentication.AuthenticationTokenRepository;
import ca.ulaval.glo4003.domain.user.authentication.NoTokenFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAuthenticationTokenRepository implements AuthenticationTokenRepository {

  private final Map<String, AuthenticationToken> tokens = new ConcurrentHashMap<>();

  @Override
  public AuthenticationToken getTokenForUser(String username) {
    if (username == null) {
      throw new NoTokenFoundException();
    }
    return Optional.ofNullable(tokens.get(username)).orElseThrow(NoTokenFoundException::new);
  }

  @Override
  public void addToken(AuthenticationToken token) {
    tokens.put(token.username, token);
  }

  @Override
  public void removeTokenOfUser(String username) {
    tokens.remove(username);
  }
}

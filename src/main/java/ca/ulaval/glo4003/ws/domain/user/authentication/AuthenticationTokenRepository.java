package ca.ulaval.glo4003.ws.domain.user.authentication;

public interface AuthenticationTokenRepository {

  AuthenticationToken getTokenForUser(String username);

  void addToken(AuthenticationToken token);
}

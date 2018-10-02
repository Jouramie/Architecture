package ca.ulaval.glo4003.domain.user.authentication;

public interface AuthenticationTokenRepository {

  AuthenticationToken getTokenForUser(String username);

  void addToken(AuthenticationToken token);

  void removeTokenOfUser(String username);
}

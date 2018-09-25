package ca.ulaval.glo4003.ws.domain.user;

import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationErrorException;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationToken;
import ca.ulaval.glo4003.ws.domain.user.authentication.AuthenticationTokenFactory;
import java.util.ArrayList;
import java.util.List;

public class User {
  private final String username;
  private final String password;
  private final UserRole role;
  private final List<AuthenticationToken> emittedTokens = new ArrayList<>();

  public User(String username, String password, UserRole role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public UserRole getRole() {
    return role;
  }

  public AuthenticationToken authenticateByPassword(String password, AuthenticationTokenFactory tokenFactory) {
    if (!this.password.equals(password)) {
      throw new AuthenticationErrorException();
    }
    AuthenticationToken token = tokenFactory.createToken(username);

    emittedTokens.add(token);
    return token;
  }

  public boolean isAuthenticatedBy(AuthenticationToken token) {
    return emittedTokens.stream().anyMatch(savedToken -> savedToken.equals(token));
  }
}

package ca.ulaval.glo4003.ws.domain.user.authentication;

import java.util.Objects;

public class AuthenticationToken {
  public final String token;
  public final String username;

  public AuthenticationToken(String token, String username) {
    this.token = token;
    this.username = username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AuthenticationToken)) {
      return false;
    }
    AuthenticationToken other = (AuthenticationToken) o;
    return Objects.equals(token, other.token) && Objects.equals(username, other.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, username);
  }
}

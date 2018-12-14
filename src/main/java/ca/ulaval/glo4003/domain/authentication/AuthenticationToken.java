package ca.ulaval.glo4003.domain.authentication;

import java.util.Objects;

public class AuthenticationToken {
  public final String token;
  public final String email;

  public AuthenticationToken(String token, String email) {
    this.token = token;
    this.email = email;
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
    return Objects.equals(token, other.token) && Objects.equals(email, other.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, email);
  }
}

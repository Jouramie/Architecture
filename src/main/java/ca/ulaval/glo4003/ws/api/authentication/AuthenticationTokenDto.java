package ca.ulaval.glo4003.ws.api.authentication;

public class AuthenticationTokenDto {

  public final String email;

  public final String token;

  public AuthenticationTokenDto(String email, String token) {
    this.email = email;
    this.token = token;
  }
}

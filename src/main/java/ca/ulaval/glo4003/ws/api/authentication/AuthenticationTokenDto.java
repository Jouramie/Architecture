package ca.ulaval.glo4003.ws.api.authentication;

public class AuthenticationTokenDto {

  public final String username;

  public final String token;

  public AuthenticationTokenDto(String username, String token) {
    this.username = username;
    this.token = token;
  }
}

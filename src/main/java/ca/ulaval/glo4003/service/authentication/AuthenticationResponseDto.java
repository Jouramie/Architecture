package ca.ulaval.glo4003.service.authentication;

public class AuthenticationResponseDto {
  public final String token;

  public AuthenticationResponseDto(String token) {
    this.token = token;
  }
}

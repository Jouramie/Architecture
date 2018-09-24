package ca.ulaval.glo4003.ws.api.authentication;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "AuthenticationRequestDto",
    description = "Authentication request"
)
public class AuthenticationRequestDto {

  @Schema(description = "Username")
  public final String username;

  @Schema(description = "Password")
  public final String password;

  public AuthenticationRequestDto(String username, String password) {
    this.username = username;
    this.password = password;
  }
}

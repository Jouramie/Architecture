package ca.ulaval.glo4003.ws.api.authentication;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "AuthenticationResponseDto",
    description = "Authentication response"
)
public class AuthenticationResponseDto {

  @Schema(description = "Authentication token")
  public final String token;

  public AuthenticationResponseDto(String token) {
    this.token = token;
  }
}

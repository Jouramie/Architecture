package ca.ulaval.glo4003.ws.api.authentication;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "UserResponse",
    description = "Basic user information"
)
public class UserDto {

  @Schema(description = "Username")
  public final String username;

  @Schema(description = "User role")
  public final UserRole role;

  public UserDto(String username, UserRole role) {
    this.username = username;
    this.role = role;
  }
}

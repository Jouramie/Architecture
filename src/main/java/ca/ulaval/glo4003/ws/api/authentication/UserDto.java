package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.ws.domain.user.UserRole;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

  @JsonCreator
  public UserDto(@JsonProperty("username") String username,
                 @JsonProperty("role") UserRole role) {
    this.username = username;
    this.role = role;
  }
}

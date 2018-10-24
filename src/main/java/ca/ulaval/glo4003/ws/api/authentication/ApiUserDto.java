package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.domain.user.UserRole;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "UserResponse",
    description = "Basic user information"
)
public class ApiUserDto {

  @Schema(description = "Email")
  public final String email;

  @Schema(description = "User role")
  public final UserRole role;

  @JsonCreator
  public ApiUserDto(@JsonProperty("email") String email,
                    @JsonProperty("role") UserRole role) {
    this.email = email;
    this.role = role;
  }
}

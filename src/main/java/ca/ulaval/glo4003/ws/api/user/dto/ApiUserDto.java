package ca.ulaval.glo4003.ws.api.user.dto;

import ca.ulaval.glo4003.domain.user.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "User",
    description = "Basic user information"
)
public class ApiUserDto {

  @Schema(description = "Email")
  public final String email;

  @Schema(description = "User role")
  public final UserRole role;

  @Schema(
      description = "Trade limit of the user",
      nullable = true
  )
  public final ApiUserLimitDto limit;

  public ApiUserDto(String email, UserRole role, ApiUserLimitDto limit) {
    this.email = email;
    this.role = role;
    this.limit = limit;
  }
}

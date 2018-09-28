package ca.ulaval.glo4003.ws.api.authentication;

import ca.ulaval.glo4003.ws.domain.user.UserRole;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(
    name = "UserCreationRequest",
    description = "User creation form"
)
public class UserCreationDto {

  @Schema(description = "Requested username")
  @NotBlank
  public final String username;

  @Schema(description = "Password")
  @Size(min = 1)
  public final String password;

  @Schema(description = "User role")
  @NotNull
  public final UserRole role;

  @JsonCreator
  public UserCreationDto(@JsonProperty("username") String username,
                         @JsonProperty("password") String password,
                         @JsonProperty("role") UserRole role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }
}

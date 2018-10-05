package ca.ulaval.glo4003.ws.api.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

@Schema(
    name = "AuthenticationRequestDto",
    description = "Authentication request"
)
public class AuthenticationRequestDto {

  @Schema(description = "Email")
  @NotNull
  public final String email;

  @Schema(description = "Password")
  @NotNull
  public final String password;

  @JsonCreator
  public AuthenticationRequestDto(@JsonProperty("email") String email,
                                  @JsonProperty("password") String password) {
    this.email = email;
    this.password = password;
  }
}

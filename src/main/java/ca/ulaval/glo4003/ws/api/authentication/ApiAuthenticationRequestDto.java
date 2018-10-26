package ca.ulaval.glo4003.ws.api.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(
    name = "Authentication request"
)
public class ApiAuthenticationRequestDto {

  @NotNull
  @NotBlank
  public final String email;

  @NotNull
  @Size(min = 8)
  public final String password;

  @JsonCreator
  public ApiAuthenticationRequestDto(
      @JsonProperty("email") String email,
      @JsonProperty("password") String password) {
    this.email = email;
    this.password = password;
  }
}

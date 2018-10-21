package ca.ulaval.glo4003.ws.api.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "Authentication response"
)
public class AuthenticationResponseDto {

  @Schema(description = "Authentication token")
  public final String token;

  @JsonCreator
  public AuthenticationResponseDto(
      @JsonProperty("token")
          String token) {
    this.token = token;
  }
}

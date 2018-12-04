package ca.ulaval.glo4003.ws.api.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(
    name = "Investor creation request"
)
public class InvestorCreationDto {

  @Size(min=1)
  public final String email;

  @NotNull
  @Size(min = 8)
  public final String password;

  @JsonCreator
  public InvestorCreationDto(
      @JsonProperty("email") String email,
      @JsonProperty("password") String password) {
    this.email = email;
    this.password = password;
  }
}

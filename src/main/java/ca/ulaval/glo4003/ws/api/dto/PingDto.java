package ca.ulaval.glo4003.ws.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
    name = "PingResponse",
    description = "Ping response containing the version, the current time and an echo parameter."
)
public class PingDto {
  @Schema(description = "Current version of the API")
  public final String version;
  @Schema(description = "Current date of the server")
  public final LocalDateTime date;
  @Schema(description = "Echo message")
  public final String echo;

  private PingDto() {
    this("test");
  }

  public PingDto(String echo) {
    version = "0.0.1";
    date = LocalDateTime.now();
    this.echo = echo;
  }
}

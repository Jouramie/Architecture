package ca.ulaval.glo4003.ws.api.ping;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
    name = "Ping"
)
public class PingDto {
  @Schema(description = "Current version of the API.")
  public final String version;
  @Schema(description = "Current date of the server.")
  public final LocalDateTime date;
  @Schema(description = "Echo message sent to the server.")
  public final String echo;

  public PingDto(LocalDateTime date, String echo) {
    version = "0.0.1";
    this.date = date;
    this.echo = echo;
  }
}

package ca.ulaval.glo4003.ws.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
    name = "User limit",
    description = "Partial representation of a transaction limit."
)
public abstract class ApiLimitDto {
  @Schema(
      description = "The date when then limit start applying."
  )
  public final LocalDateTime begin;
  @Schema(
      description = "The date when the limit stop applying."
  )
  public final LocalDateTime end;

  public ApiLimitDto(LocalDateTime begin, LocalDateTime end) {
    this.begin = begin;
    this.end = end;
  }
}

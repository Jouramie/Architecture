package ca.ulaval.glo4003.ws.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
    name = "User limit",
    description = "Partial representation of a transaction limit."
)
public abstract class ApiUserLimitDto {
  @Schema(
      description = "The date when then limit start applying."
  )
  public final LocalDateTime beginDate;
  @Schema(
      description = "The date when the limit stop applying."
  )
  public final LocalDateTime endDate;

  public ApiUserLimitDto(LocalDateTime beginDate, LocalDateTime endDate) {
    this.beginDate = beginDate;
    this.endDate = endDate;
  }
}

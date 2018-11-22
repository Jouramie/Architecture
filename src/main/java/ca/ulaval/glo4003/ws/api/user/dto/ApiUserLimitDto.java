package ca.ulaval.glo4003.ws.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(
    name = "Limit",
    description = "Partial representation of a transaction limit."
)
public abstract class ApiUserLimitDto {
  @Schema(
      description = "The date when then limit start applying."
  )
  public final Date beginDate;
  @Schema(
      description = "The date when the limit stop applying."
  )
  public final Date endDate;

  public ApiUserLimitDto(Date beginDate, Date endDate) {
    this.beginDate = beginDate;
    this.endDate = endDate;
  }
}

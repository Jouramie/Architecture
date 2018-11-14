package ca.ulaval.glo4003.ws.api.user.dto;

import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(
    name = "User limit",
    description = "Partial representation of a transaction limit."
)
public abstract class ApiUserLimitDto {
  public final ApplicationPeriod applicationPeriod;
  @Schema(
      description = "The date when then limit start applying."
  )
  public final Date beginDate;
  @Schema(
      description = "The date when the limit stop applying."
  )
  public final Date endDate;

  public ApiUserLimitDto(ApplicationPeriod applicationPeriod, Date beginDate, Date endDate) {
    this.applicationPeriod = applicationPeriod;
    this.beginDate = beginDate;
    this.endDate = endDate;
  }
}

package ca.ulaval.glo4003.ws.api.user.dto;

import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(
    name = "Money amount user limit",
    description = "Representation of a maximal money amount per transaction limit."
)
public class ApiUserMoneyAmountLimitDto extends ApiUserLimitDto {
  @Schema(
      description = "The maximal money amount the user can spend in a single transaction."
  )
  public final double maximalMoneySpent;

  public ApiUserMoneyAmountLimitDto(double maximalMoneySpent, ApplicationPeriod applicationPeriod, Date beginDate, Date endDate) {
    super(applicationPeriod, beginDate, endDate);
    this.maximalMoneySpent = maximalMoneySpent;
  }
}

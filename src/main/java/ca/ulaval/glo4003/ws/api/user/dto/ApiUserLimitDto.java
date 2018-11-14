package ca.ulaval.glo4003.ws.api.user.dto;

import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(
    name = "User limit",
    description = "Representation of a transaction limit. Might apply on the money amount the user "
        + "can spend or the number of stock the user can purchase. In either case, the other field "
        + "will not have a value."
)
public class ApiUserLimitDto {
  public final ApplicationPeriod applicationPeriod;
  @Schema(
      description = "The date when then limit start applying."
  )
  public final Date beginDate;
  @Schema(
      description = "The date when the limit stop applying."
  )
  public final Date endDate;

  @Schema(
      description = "The maximal money amount the user can spend in a single transaction.",
      nullable = true
  )
  public final Double maximalMoneySpent;
  @Schema(
      description = "The maximal quantity of stock the user can buy in a single transaction.",
      nullable = true
  )
  public final Integer maximalStockQuantity;

  public ApiUserLimitDto(double maximalMoneySpent, ApplicationPeriod applicationPeriod,
                         Date beginDate, Date endDate) {
    this.maximalMoneySpent = maximalMoneySpent;
    maximalStockQuantity = null;
    this.applicationPeriod = applicationPeriod;
    this.beginDate = beginDate;
    this.endDate = endDate;
  }

  public ApiUserLimitDto(int maximalStockQuantity, ApplicationPeriod applicationPeriod,
                         Date beginDate, Date endDate) {
    maximalMoneySpent = null;
    this.maximalStockQuantity = maximalStockQuantity;
    this.applicationPeriod = applicationPeriod;
    this.beginDate = beginDate;
    this.endDate = endDate;
  }
}

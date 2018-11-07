package ca.ulaval.glo4003.ws.api.user;

import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Schema(
    name = "User transaction limit creation request"
)
public class UserLimitCreationDto {
  @NotNull
  public final ApplicationPeriod applicationPeriod;
  @Schema(
      description = "The date when then limit start applying.",
      defaultValue = "Now"
  )
  public final Date beginDate;
  @NotNull
  @Future
  @Schema(
      description = "The date when the limit stop applying."
  )
  public final Date endDate;
  @PositiveOrZero
  @Schema(
      description = "The maximal money the user can spend over the limit application period."
  )
  public final Double maximalMoneySpent;
  @PositiveOrZero
  @Schema(
      description = "The maximal quantity of stock the user can buy over the limit application "
          + "period."
  )
  public final Integer maximalStockQuantity;

  @JsonCreator
  public UserLimitCreationDto(
      @JsonProperty("applicationPeriod") ApplicationPeriod applicationPeriod,
      @JsonProperty("beginDate") Date beginDate,
      @JsonProperty("endDate") Date endDate,
      @JsonProperty("maximalMoneySpent") Double maximalMoneySpent,
      @JsonProperty("maximalStockQuantity") Integer maximalStockQuantity) {
    this.applicationPeriod = applicationPeriod;
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.maximalMoneySpent = maximalMoneySpent;
    this.maximalStockQuantity = maximalStockQuantity;
  }
}

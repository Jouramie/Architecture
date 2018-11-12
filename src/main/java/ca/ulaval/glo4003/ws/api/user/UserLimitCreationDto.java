package ca.ulaval.glo4003.ws.api.user;

import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Schema(
    name = "User limit creation request",
    description = "Might apply on the money amount the user can spend or the number of stock the "
        + "user can purchase. In either case, the other field will not have a value."
)
public class UserLimitCreationDto {
  @NotNull
  public final ApplicationPeriod applicationPeriod;
  @PositiveOrZero
  @Schema(
      description = "The maximal money amount the user can spend in a single transaction."
  )
  public final Double maximalMoneySpent;
  @PositiveOrZero
  @Schema(
      description = "The maximal quantity of stock the user can buy in a single transaction."
  )
  public final Integer maximalStockQuantity;

  @JsonCreator
  public UserLimitCreationDto(
      @JsonProperty("applicationPeriod") ApplicationPeriod applicationPeriod,
      @JsonProperty("maximalMoneySpent") Double maximalMoneySpent,
      @JsonProperty("maximalStockQuantity") Integer maximalStockQuantity) {
    this.applicationPeriod = applicationPeriod;
    this.maximalMoneySpent = maximalMoneySpent;
    this.maximalStockQuantity = maximalStockQuantity;
  }
}

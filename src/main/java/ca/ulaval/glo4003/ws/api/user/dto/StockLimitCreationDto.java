package ca.ulaval.glo4003.ws.api.user.dto;

import ca.ulaval.glo4003.domain.user.limit.ApplicationPeriod;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Schema(
    name = "Limit creation request",
    description = "Might apply on the money amount the user can spend or the number of stock the "
        + "user can purchase. In either case, the other field will not have a value."
)
public class StockLimitCreationDto {
  @NotNull
  public final ApplicationPeriod applicationPeriod;

  @NotNull
  @Min(value = 0)
  @Schema(
      description = "The maximum quantity of stock the user can buy in a single transaction."
  )
  public final int stockQuantity;

  @JsonCreator
  public StockLimitCreationDto(
      @JsonProperty("applicationPeriod") ApplicationPeriod applicationPeriod,
      @JsonProperty("stockQuantity") int stockQuantity) {
    this.applicationPeriod = applicationPeriod;
    this.stockQuantity = stockQuantity;
  }
}

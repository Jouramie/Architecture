package ca.ulaval.glo4003.ws.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
    name = "Stock user limit",
    description = "Representation of a maximal stock per transaction limit."
)
public class ApiUserStockLimitDto extends ApiUserLimitDto {
  @Schema(
      description = "The maximal quantity of stock the user can buy in a single transaction."
  )
  public final int maximalStockQuantity;

  public ApiUserStockLimitDto(int maximalStockQuantity, LocalDateTime beginDate, LocalDateTime endDate) {
    super(beginDate, endDate);
    this.maximalStockQuantity = maximalStockQuantity;
  }
}

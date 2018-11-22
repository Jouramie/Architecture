package ca.ulaval.glo4003.ws.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
    name = "Stock user limit",
    description = "Representation of a stock quantity per transaction limit."
)
public class ApiStockLimitDto extends ApiLimitDto {
  @Schema(
      description = "The maximum quantity of stock the user can buy in a single transaction."
  )
  public final int stockQuantity;

  public ApiStockLimitDto(LocalDateTime from, LocalDateTime to, int stockQuantity) {
    super(from, to);
    this.stockQuantity = stockQuantity;
  }
}

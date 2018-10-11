package ca.ulaval.glo4003.ws.api.portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
    name = "PortfolioItemResponseDto",
    description = "Portfolio stock response containing the title, current value and quantity of a stock."
)
public class PortfolioItemResponseDto {
  @Schema(description = "Title")
  public final String title;
  @Schema(description = "Current value")
  public final BigDecimal currentValue;
  @Schema(description = "Quantity")
  public final int quantity;

  public PortfolioItemResponseDto(String title, BigDecimal currentValue, int quantity) {
    this.title = title;
    this.currentValue = currentValue;
    this.quantity = quantity;
  }
}

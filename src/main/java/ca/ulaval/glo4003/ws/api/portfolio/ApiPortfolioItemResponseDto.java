package ca.ulaval.glo4003.ws.api.portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
    name = "Portfolio item"
)
public class ApiPortfolioItemResponseDto {
  public final String title;
  public final BigDecimal currentValue;
  public final int quantity;

  public ApiPortfolioItemResponseDto(String title, BigDecimal currentValue, int quantity) {
    this.title = title;
    this.currentValue = currentValue;
    this.quantity = quantity;
  }
}
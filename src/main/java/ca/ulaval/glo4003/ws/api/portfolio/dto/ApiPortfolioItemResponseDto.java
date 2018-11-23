package ca.ulaval.glo4003.ws.api.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
    name = "Portfolio item"
)
public class ApiPortfolioItemResponseDto {
  public final String title;
  public final BigDecimal value;
  public final int quantity;

  public ApiPortfolioItemResponseDto(String title, BigDecimal value, int quantity) {
    this.title = title;
    this.value = value;
    this.quantity = quantity;
  }
}

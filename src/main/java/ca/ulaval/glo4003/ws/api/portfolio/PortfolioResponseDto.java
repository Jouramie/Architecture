package ca.ulaval.glo4003.ws.api.portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(
    name = "PortfolioResponseDto",
    description = "Portfolio response containing all stocks and the current total value of the portfolio."
)
public class PortfolioResponseDto {
  @Schema(description = "Portfolio stocks")
  public final List<PortfolioItemResponseDto> stocks;
  @Schema(description = "Current total value")
  public final BigDecimal currentTotalValue;

  public PortfolioResponseDto(List<PortfolioItemResponseDto> stocks, BigDecimal currentTotalValue) {
    this.stocks = stocks;
    this.currentTotalValue = currentTotalValue;
  }
}

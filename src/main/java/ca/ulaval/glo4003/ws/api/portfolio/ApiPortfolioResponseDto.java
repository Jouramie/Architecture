package ca.ulaval.glo4003.ws.api.portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(
    name = "Portfolio"
)
public class ApiPortfolioResponseDto {
  public final List<ApiPortfolioItemResponseDto> stocks;
  public final BigDecimal currentTotalValue;

  public ApiPortfolioResponseDto(List<ApiPortfolioItemResponseDto> stocks, BigDecimal currentTotalValue) {
    this.stocks = stocks;
    this.currentTotalValue = currentTotalValue;
  }
}

package ca.ulaval.glo4003.ws.api.portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(
    name = "Portfolio"
)
public class PortfolioResponseDto {
  public final List<PortfolioItemResponseDto> stocks;
  public final BigDecimal currentTotalValue;

  public PortfolioResponseDto(List<PortfolioItemResponseDto> stocks, BigDecimal currentTotalValue) {
    this.stocks = stocks;
    this.currentTotalValue = currentTotalValue;
  }
}

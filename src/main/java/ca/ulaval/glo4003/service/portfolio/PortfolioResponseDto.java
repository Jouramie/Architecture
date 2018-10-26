package ca.ulaval.glo4003.service.portfolio;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioResponseDto {
  public final List<PortfolioItemResponseDto> stocks;
  public final BigDecimal currentTotalValue;

  public PortfolioResponseDto(List<PortfolioItemResponseDto> stocks, BigDecimal currentTotalValue) {
    this.stocks = stocks;
    this.currentTotalValue = currentTotalValue;
  }
}

package ca.ulaval.glo4003.service.portfolio.dto;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioDto {
  public final List<PortfolioItemDto> stocks;
  public final BigDecimal currentTotalValue;

  public PortfolioDto(List<PortfolioItemDto> stocks, BigDecimal currentTotalValue) {
    this.stocks = stocks;
    this.currentTotalValue = currentTotalValue;
  }
}

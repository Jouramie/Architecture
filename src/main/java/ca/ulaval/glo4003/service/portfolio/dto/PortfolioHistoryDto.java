package ca.ulaval.glo4003.service.portfolio.dto;

import java.util.List;

public class PortfolioHistoryDto {
  public final List<HistoricalPortfolioDto> historicalPortfolios;

  public PortfolioHistoryDto(List<HistoricalPortfolioDto> historicalPortfolios) {
    this.historicalPortfolios = historicalPortfolios;
  }
}

package ca.ulaval.glo4003.service.portfolio.dto;

import java.util.List;

public class PortfolioReportDto {
  public final List<HistoricalPortfolioDto> history;
  public final String mostIncreasingStock;
  public final String mostDecreasingStock;

  public PortfolioReportDto(List<HistoricalPortfolioDto> history, String mostIncreasingStock,
                            String mostDecreasingStock) {
    this.history = history;
    this.mostIncreasingStock = mostIncreasingStock;
    this.mostDecreasingStock = mostDecreasingStock;
  }
}

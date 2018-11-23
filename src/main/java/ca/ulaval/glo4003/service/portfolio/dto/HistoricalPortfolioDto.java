package ca.ulaval.glo4003.service.portfolio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class HistoricalPortfolioDto {
  public final LocalDate date;
  public final List<PortfolioItemDto> stocks;
  public final BigDecimal totalValue;

  public HistoricalPortfolioDto(LocalDate date, List<PortfolioItemDto> stocks, BigDecimal totalValue) {
    this.date = date;
    this.stocks = stocks;
    this.totalValue = totalValue;
  }
}

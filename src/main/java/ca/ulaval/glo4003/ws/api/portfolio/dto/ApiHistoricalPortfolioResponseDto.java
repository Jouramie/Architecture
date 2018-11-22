package ca.ulaval.glo4003.ws.api.portfolio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ApiHistoricalPortfolioResponseDto {
  public final LocalDate date;
  public final List<ApiPortfolioItemResponseDto> stocks;
  public final BigDecimal totalValue;

  public ApiHistoricalPortfolioResponseDto(LocalDate date, List<ApiPortfolioItemResponseDto> stocks, BigDecimal totalValue) {
    this.date = date;
    this.stocks = stocks;
    this.totalValue = totalValue;
  }
}

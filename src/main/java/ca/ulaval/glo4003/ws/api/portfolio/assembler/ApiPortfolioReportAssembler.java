package ca.ulaval.glo4003.ws.api.portfolio.assembler;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.service.portfolio.dto.HistoricalPortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioItemDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioReportDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiHistoricalPortfolioResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioItemResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioReportResponseDto;
import java.util.List;

@Component
public class ApiPortfolioReportAssembler {
  public ApiPortfolioReportResponseDto toDto(PortfolioReportDto reportDto) {
    List<ApiHistoricalPortfolioResponseDto> portfolios = reportDto.history.stream()
        .map(this::historicalPortfolioToDto).collect(toList());
    return new ApiPortfolioReportResponseDto(portfolios, reportDto.mostIncreasingStock,
        reportDto.mostDecreasingStock);
  }

  private ApiHistoricalPortfolioResponseDto historicalPortfolioToDto(HistoricalPortfolioDto historicalPortfolioDto) {
    List<ApiPortfolioItemResponseDto> items = historicalPortfolioDto.stocks.stream().map(this::itemToDto).collect(toList());
    return new ApiHistoricalPortfolioResponseDto(historicalPortfolioDto.date, items, historicalPortfolioDto.totalValue);
  }

  private ApiPortfolioItemResponseDto itemToDto(PortfolioItemDto historicalPortfolioDto) {
    return new ApiPortfolioItemResponseDto(historicalPortfolioDto.title, historicalPortfolioDto.currentValue,
        historicalPortfolioDto.quantity);
  }
}

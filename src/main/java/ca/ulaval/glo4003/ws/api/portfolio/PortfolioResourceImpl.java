package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.service.InternalErrorException;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.portfolio.PortfolioService;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioReportDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioReportResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.SinceParameter;
import java.time.LocalDate;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

@Resource
public class PortfolioResourceImpl implements PortfolioResource {
  private final PortfolioService portfolioService;
  private final ApiPortfolioAssembler apiPortfolioAssembler;
  private final ApiPortfolioReportAssembler apiPortfolioReportAssembler;
  private final DateService dateService;

  @Inject
  public PortfolioResourceImpl(PortfolioService portfolioService, ApiPortfolioAssembler apiPortfolioAssembler,
                               ApiPortfolioReportAssembler apiPortfolioReportAssembler, DateService dateService) {
    this.portfolioService = portfolioService;
    this.apiPortfolioAssembler = apiPortfolioAssembler;
    this.apiPortfolioReportAssembler = apiPortfolioReportAssembler;
    this.dateService = dateService;
  }

  @Override
  public ApiPortfolioResponseDto getPortfolio() {
    PortfolioDto responseDto = portfolioService.getPortfolio();
    return apiPortfolioAssembler.toDto(responseDto);
  }

  @Override
  public ApiPortfolioReportResponseDto getPortfolioReport(String since) {
    try {
      SinceParameter sinceValue = SinceParameter.valueOf(since);
      LocalDate from = getFromDate(sinceValue);
      PortfolioReportDto reportDto = portfolioService.getPortfolioReport(from);
      return apiPortfolioReportAssembler.toDto(reportDto);
    } catch (NullPointerException | IllegalArgumentException e) {
      throw new BadRequestException("Invalid 'since' parameter");
    }
  }

  private LocalDate getFromDate(SinceParameter sinceParameter) {
    switch (sinceParameter) {
      case LAST_FIVE_DAYS:
        return dateService.getFiveDaysAgo();
      case LAST_THIRTY_DAYS:
        return dateService.getThirtyDaysAgo();
      case LAST_YEAR:
        return dateService.getOneYearAgo();
      default:
        throw new InternalErrorException("Missing enum case in PortfolioResource.");
    }
  }
}

package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.service.date.Since;
import ca.ulaval.glo4003.service.portfolio.PortfolioService;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioReportDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioReportResponseDto;
import ca.ulaval.glo4003.ws.api.portfolio.dto.ApiPortfolioResponseDto;
import ca.ulaval.glo4003.ws.api.util.SinceParameterConverter;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class PortfolioResourceImpl implements PortfolioResource {
  private final PortfolioService portfolioService;
  private final ApiPortfolioAssembler apiPortfolioAssembler;
  private final ApiPortfolioReportAssembler apiPortfolioReportAssembler;

  @Inject
  public PortfolioResourceImpl(PortfolioService portfolioService,
                               ApiPortfolioAssembler apiPortfolioAssembler,
                               ApiPortfolioReportAssembler apiPortfolioReportAssembler) {
    this.portfolioService = portfolioService;
    this.apiPortfolioAssembler = apiPortfolioAssembler;
    this.apiPortfolioReportAssembler = apiPortfolioReportAssembler;
  }

  @Override
  public ApiPortfolioResponseDto getPortfolio() {
    PortfolioDto responseDto = portfolioService.getPortfolio();
    return apiPortfolioAssembler.toDto(responseDto);
  }

  @Override
  public ApiPortfolioReportResponseDto getPortfolioReport(String since) {
    Since sinceValue = SinceParameterConverter.convertSinceParameter(since);
    PortfolioReportDto reportDto = portfolioService.getPortfolioReport(sinceValue);
    return apiPortfolioReportAssembler.toDto(reportDto);
  }
}

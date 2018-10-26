package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.service.portfolio.PortfolioResponseDto;
import ca.ulaval.glo4003.service.portfolio.PortfolioService;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class PortfolioResourceImpl implements PortfolioResource {
  private final PortfolioService portfolioService;
  private final ApiPortfolioAssembler apiPortfolioAssembler;

  @Inject
  public PortfolioResourceImpl(PortfolioService portfolioService, ApiPortfolioAssembler apiPortfolioAssembler) {
    this.portfolioService = portfolioService;
    this.apiPortfolioAssembler = apiPortfolioAssembler;
  }

  @Override
  public ApiPortfolioResponseDto getPortfolio() {
    PortfolioResponseDto responseDto = portfolioService.getPortfolio();
    return apiPortfolioAssembler.toDto(responseDto);
  }
}

package ca.ulaval.glo4003.ws.api.portfolio;

import ca.ulaval.glo4003.service.portfolio.PortfolioService;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class PortfolioResourceImpl implements PortfolioResource {
  private final PortfolioService portfolioService;

  @Inject
  public PortfolioResourceImpl(PortfolioService portfolioService) {
    this.portfolioService = portfolioService;
  }

  @Override
  public PortfolioResponseDto getPortfolio() {
    return portfolioService.getPortfolio();
  }
}

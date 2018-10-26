package ca.ulaval.glo4003.service.portfolio;

import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.service.Component;
import javax.inject.Inject;

@Component
public class PortfolioService {
  private final CurrentUserSession currentUserSession;
  private final PortfolioAssembler portfolioAssembler;

  @Inject
  public PortfolioService(CurrentUserSession currentUserSession, PortfolioAssembler portfolioAssembler) {
    this.currentUserSession = currentUserSession;
    this.portfolioAssembler = portfolioAssembler;
  }

  public PortfolioResponseDto getPortfolio() throws InvalidPortfolioException {
    PortfolioResponseDto dto;
    try {
      Portfolio portfolio = currentUserSession.getCurrentUser().getPortfolio();
      dto = portfolioAssembler.toDto(portfolio);
    } catch (InvalidStockInPortfolioException e) {
      throw new InvalidPortfolioException();
    }
    return dto;
  }
}

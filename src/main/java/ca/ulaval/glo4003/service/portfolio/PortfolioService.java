package ca.ulaval.glo4003.service.portfolio;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.portfolio.HistoricalPortfolio;
import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioHistoryDto;
import java.time.LocalDate;
import java.util.TreeSet;
import javax.inject.Inject;

@Component
public class PortfolioService {
  private final CurrentUserSession currentUserSession;
  private final PortfolioAssembler portfolioAssembler;
  private final HistoricalPortfolioAssembler historicalPortfolioAssembler;
  private final Clock clock;

  @Inject
  public PortfolioService(CurrentUserSession currentUserSession,
                          PortfolioAssembler portfolioAssembler,
                          HistoricalPortfolioAssembler historicalPortfolioAssembler,
                          Clock clock) {
    this.currentUserSession = currentUserSession;
    this.portfolioAssembler = portfolioAssembler;
    this.historicalPortfolioAssembler = historicalPortfolioAssembler;
    this.clock = clock;
  }

  public PortfolioDto getPortfolio() throws InvalidPortfolioException {
    PortfolioDto dto;
    try {
      Portfolio portfolio = currentUserSession.getCurrentUser().getPortfolio();
      dto = portfolioAssembler.toDto(portfolio);
    } catch (InvalidStockInPortfolioException e) {
      throw new InvalidPortfolioException();
    }
    return dto;
  }

  public PortfolioHistoryDto getPortfolioHistory(LocalDate from) {
    try {
      Investor investor = currentUserSession.getCurrentUser();
      TreeSet<HistoricalPortfolio> portfolios = investor.getPortfolio().getHistory(from, clock.getCurrentTime().toLocalDate());
      return historicalPortfolioAssembler.toDto(portfolios);
    } catch (StockNotFoundException | NoStockValueFitsCriteriaException e) {
      throw new InvalidPortfolioException();
    }
  }
}

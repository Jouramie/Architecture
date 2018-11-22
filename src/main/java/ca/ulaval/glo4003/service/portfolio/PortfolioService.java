package ca.ulaval.glo4003.service.portfolio;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.portfolio.HistoricalPortfolio;
import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioDto;
import ca.ulaval.glo4003.service.portfolio.dto.PortfolioReportDto;
import java.time.LocalDate;
import java.util.TreeSet;
import javax.inject.Inject;

@Component
public class PortfolioService {
  private final CurrentUserSession currentUserSession;
  private final PortfolioAssembler portfolioAssembler;
  private final PortfolioReportAssembler portfolioReportAssembler;
  private final Clock clock;
  private final StockRepository stockRepository;

  @Inject
  public PortfolioService(CurrentUserSession currentUserSession,
                          PortfolioAssembler portfolioAssembler,
                          PortfolioReportAssembler portfolioReportAssembler,
                          Clock clock,
                          StockRepository stockRepository) {
    this.currentUserSession = currentUserSession;
    this.portfolioAssembler = portfolioAssembler;
    this.portfolioReportAssembler = portfolioReportAssembler;
    this.clock = clock;
    this.stockRepository = stockRepository;
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

  public PortfolioReportDto getPortfolioReport(LocalDate from) {
    try {
      User user = currentUserSession.getCurrentUser();
      Portfolio portfolio = user.getPortfolio();
      TreeSet<HistoricalPortfolio> portfolios = portfolio.getHistory(from, clock.getCurrentTime().toLocalDate());
      String mostIncreasingStockTitle = portfolio.getMostIncreasingStockTitle(from, stockRepository);
      String mostDecreasingStockTitle = portfolio.getMostDecreasingStockTitle(from, stockRepository);
      return portfolioReportAssembler.toDto(portfolios, mostIncreasingStockTitle, mostDecreasingStockTitle);
    } catch (StockNotFoundException | NoStockValueFitsCriteriaException | InvalidStockInPortfolioException e) {
      throw new InvalidPortfolioException();
    }
  }
}

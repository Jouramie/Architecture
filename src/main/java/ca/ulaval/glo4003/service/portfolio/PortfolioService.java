package ca.ulaval.glo4003.service.portfolio;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.clock.ReadableClock;
import ca.ulaval.glo4003.domain.portfolio.HistoricalPortfolio;
import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.date.Since;
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
  private final ReadableClock clock;
  private final StockRepository stockRepository;
  private final DateService dateService;

  @Inject
  public PortfolioService(CurrentUserSession currentUserSession,
                          PortfolioAssembler portfolioAssembler,
                          PortfolioReportAssembler portfolioReportAssembler,
                          ReadableClock clock,
                          StockRepository stockRepository,
                          DateService dateService) {
    this.currentUserSession = currentUserSession;
    this.portfolioAssembler = portfolioAssembler;
    this.portfolioReportAssembler = portfolioReportAssembler;
    this.clock = clock;
    this.stockRepository = stockRepository;
    this.dateService = dateService;
  }

  public PortfolioDto getPortfolio() throws InvalidPortfolioException {
    PortfolioDto dto;
    try {
      Portfolio portfolio = currentUserSession.getCurrentUser(Investor.class).getPortfolio();
      dto = portfolioAssembler.toDto(portfolio);
    } catch (InvalidStockInPortfolioException e) {
      throw new InvalidPortfolioException();
    }
    return dto;
  }

  public PortfolioReportDto getPortfolioReport(Since since) {
    try {
      LocalDate from = dateService.getDateSince(since);
      Investor investor = currentUserSession.getCurrentUser(Investor.class);
      Portfolio portfolio = investor.getPortfolio();
      TreeSet<HistoricalPortfolio> portfolios = portfolio.getHistory(from, clock.getCurrentDate());
      String mostIncreasingStockTitle = portfolio.getMostIncreasingStockTitle(from, stockRepository);
      String mostDecreasingStockTitle = portfolio.getMostDecreasingStockTitle(from, stockRepository);
      return portfolioReportAssembler.toDto(portfolios, mostIncreasingStockTitle, mostDecreasingStockTitle);
    } catch (StockNotFoundException | NoStockValueFitsCriteriaException | InvalidStockInPortfolioException e) {
      throw new InvalidPortfolioException();
    }
  }
}

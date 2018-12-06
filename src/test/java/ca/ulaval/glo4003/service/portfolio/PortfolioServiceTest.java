package ca.ulaval.glo4003.service.portfolio;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.portfolio.HistoricalPortfolio;
import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.Investor;
import java.time.LocalDate;
import java.util.TreeSet;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioServiceTest {
  private final LocalDate SOME_CURRENT_DATE = LocalDate.now();
  private final LocalDate SOME_FROM_DATE = SOME_CURRENT_DATE.minusDays(5);
  private final String SOME_MOST_INCREASING_STOCK = "MSFT";
  private final String SOME_MOST_DECREASING_STOCK = "AAPL";

  @Mock
  private Investor someCurrentInvestor;
  @Mock
  private PortfolioAssembler somePortfolioAssembler;
  @Mock
  private PortfolioReportAssembler somePortfolioReportAssembler;
  @Mock
  private Clock clock;
  @Mock
  private StockRepository someStockRepository;
  @Mock
  private Portfolio portfolio;
  @Mock
  private TreeSet<HistoricalPortfolio> somePortfolioHistory;

  private PortfolioService portfolioService;

  @Before
  public void setupPortfolioService() throws NoStockValueFitsCriteriaException, InvalidStockInPortfolioException {
    CurrentUserSession currentUserSession = new CurrentUserSession();
    currentUserSession.setCurrentUser(someCurrentInvestor);
    portfolioService = new PortfolioService(currentUserSession, somePortfolioAssembler,
        somePortfolioReportAssembler, clock, someStockRepository);

    given(someCurrentInvestor.getPortfolio()).willReturn(portfolio);
    given(portfolio.getHistory(SOME_FROM_DATE, SOME_CURRENT_DATE)).willReturn(somePortfolioHistory);
    given(portfolio.getMostIncreasingStockTitle(SOME_FROM_DATE, someStockRepository)).willReturn(SOME_MOST_INCREASING_STOCK);
    given(portfolio.getMostDecreasingStockTitle(SOME_FROM_DATE, someStockRepository)).willReturn(SOME_MOST_DECREASING_STOCK);
    given(clock.getCurrentDate()).willReturn(SOME_CURRENT_DATE);
  }

  @Test
  public void whenGetPortfolio_thenPortfolioOfCurrentUserIsRetrieved() {
    portfolioService.getPortfolio();

    verify(someCurrentInvestor).getPortfolio();
  }

  @Test
  public void whenGetPortfolio_thenPortfolioIsConvertedUsingAssembler() throws InvalidStockInPortfolioException {
    portfolioService.getPortfolio();

    verify(somePortfolioAssembler).toDto(any(Portfolio.class));
  }

  @Test
  public void whenGetPortfolioReport_thenPortfolioOfCurrentUserIsRetrieved() {
    portfolioService.getPortfolioReport(SOME_FROM_DATE);

    verify(someCurrentInvestor).getPortfolio();
  }

  @Test
  public void whenGetPortfolioReport_thenHistoryFromDateToCurrentDateIsRetrieved() {
    portfolioService.getPortfolioReport(SOME_FROM_DATE);

    verify(portfolio).getHistory(SOME_FROM_DATE, SOME_CURRENT_DATE);
  }

  @Test
  public void whenGetPortfolioReport_thenMostIncreasingStockTitleIsRetrieved()
      throws NoStockValueFitsCriteriaException, InvalidStockInPortfolioException {
    portfolioService.getPortfolioReport(SOME_FROM_DATE);

    verify(portfolio).getMostIncreasingStockTitle(SOME_FROM_DATE, someStockRepository);
  }

  @Test
  public void whenGetPortfolioReport_thenMostDecreasingStockTitleIsRetrieved()
      throws NoStockValueFitsCriteriaException, InvalidStockInPortfolioException {
    portfolioService.getPortfolioReport(SOME_FROM_DATE);

    verify(portfolio).getMostDecreasingStockTitle(SOME_FROM_DATE, someStockRepository);
  }

  @Test
  public void whenGetPortfolioReport_thenReportIsConvertedUsingAssembler()
      throws StockNotFoundException, NoStockValueFitsCriteriaException {
    portfolioService.getPortfolioReport(SOME_FROM_DATE);

    verify(somePortfolioReportAssembler).toDto(somePortfolioHistory, SOME_MOST_INCREASING_STOCK, SOME_MOST_DECREASING_STOCK);
  }

  @Test
  public void givenPortfolioAssemblerThrowsStockNotFoundException_whenGetPortfolioReport_thenExceptionIsThrown()
      throws StockNotFoundException, NoStockValueFitsCriteriaException {
    given(somePortfolioReportAssembler.toDto(any(), any(), any())).willThrow(StockNotFoundException.class);

    ThrowingCallable getPortfolioReport = () -> portfolioService.getPortfolioReport(SOME_FROM_DATE);

    assertThatThrownBy(getPortfolioReport).isInstanceOf(InvalidPortfolioException.class);
  }

  @Test
  public void givenPortfolioAssemblerThrowsCriteriaException_whenGetPortfolioReport_thenExceptionIsThrown()
      throws StockNotFoundException, NoStockValueFitsCriteriaException {
    given(somePortfolioReportAssembler.toDto(any(), any(), any())).willThrow(NoStockValueFitsCriteriaException.class);

    ThrowingCallable getPortfolioReport = () -> portfolioService.getPortfolioReport(SOME_FROM_DATE);

    assertThatThrownBy(getPortfolioReport).isInstanceOf(InvalidPortfolioException.class);
  }

  @Test
  public void givenPortfolioThrowsInvalidStockInPortfolioException_whenGetPortfolioReport_thenExceptionIsThrown()
      throws NoStockValueFitsCriteriaException, InvalidStockInPortfolioException {
    given(portfolio.getMostIncreasingStockTitle(any(), any())).willThrow(InvalidStockInPortfolioException.class);

    ThrowingCallable getPortfolioReport = () -> portfolioService.getPortfolioReport(SOME_FROM_DATE);

    assertThatThrownBy(getPortfolioReport).isInstanceOf(InvalidPortfolioException.class);
  }
}

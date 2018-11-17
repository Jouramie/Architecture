package ca.ulaval.glo4003.service.portfolio;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.NoStockValueFitsCriteriaException;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioServiceTest {
  private final LocalDateTime SOME_CURRENT_DATETIME = LocalDateTime.now();
  private final LocalDate SOME_FROM_DATE = SOME_CURRENT_DATETIME.toLocalDate().minusDays(5);

  @Mock
  private CurrentUserSession someCurrentUserSession;
  @Mock
  private User someCurrentUser;
  @Mock
  private PortfolioAssembler somePortfolioAssembler;
  @Mock
  private Clock clock;
  @Mock
  private Portfolio portfolio;

  private PortfolioService portfolioService;

  @Before
  public void setupPortfolioService() {
    portfolioService = new PortfolioService(someCurrentUserSession, somePortfolioAssembler, clock);

    given(someCurrentUserSession.getCurrentUser()).willReturn(someCurrentUser);
    given(someCurrentUser.getPortfolio()).willReturn(portfolio);
    given(clock.getCurrentTime()).willReturn(SOME_CURRENT_DATETIME);
  }

  @Test
  public void whenGetPortfolio_thenPortfolioOfCurrentUserIsRetrieved() {
    portfolioService.getPortfolio();

    verify(someCurrentUser).getPortfolio();
  }

  @Test
  public void whenGetPortfolio_thenPortfolioIsConvertedUsingAssembler() throws InvalidStockInPortfolioException {
    portfolioService.getPortfolio();

    verify(somePortfolioAssembler).toDto(any(Portfolio.class));
  }

  @Test
  public void whenGetPortfolioHistory_thenPortfolioOfCurrentUserIsRetrieved() {
    portfolioService.getPortfolioHistory(SOME_FROM_DATE);

    verify(someCurrentUser).getPortfolio();
  }

  @Test
  public void whenGetPortfolioHistory_thenHistoryFromDateToCurrentDateIsRetrieved() {
    portfolioService.getPortfolioHistory(SOME_FROM_DATE);

    verify(portfolio).getHistory(SOME_FROM_DATE, SOME_CURRENT_DATETIME.toLocalDate());
  }

  @Test
  public void whenGetPortfolioHistory_thenHistoryIsConvertedUsingAssembler() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    portfolioService.getPortfolioHistory(SOME_FROM_DATE);

    verify(somePortfolioAssembler).toDto(any(TreeSet.class));
  }

  @Test
  public void givenPortfolioAssemblerThrowsStockNotFoundException_whenGetPortfolioHistory_thenConvertException() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    given(somePortfolioAssembler.toDto(any(TreeSet.class))).willThrow(StockNotFoundException.class);

    assertThatThrownBy(() -> portfolioService.getPortfolioHistory(SOME_FROM_DATE)).isInstanceOf(InvalidPortfolioException.class);
  }

  @Test
  public void givenPortfolioAssemblerThrowsCriteriaException_whenGetPortfolioHistory_thenConvertException() throws StockNotFoundException, NoStockValueFitsCriteriaException {
    given(somePortfolioAssembler.toDto(any(TreeSet.class))).willThrow(NoStockValueFitsCriteriaException.class);

    assertThatThrownBy(() -> portfolioService.getPortfolioHistory(SOME_FROM_DATE)).isInstanceOf(InvalidPortfolioException.class);
  }
}

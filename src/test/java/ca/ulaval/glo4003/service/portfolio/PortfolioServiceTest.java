package ca.ulaval.glo4003.service.portfolio;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.portfolio.InvalidStockInPortfolioException;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioServiceTest {
  @Mock
  private CurrentUserSession someCurrentUserSession;
  @Mock
  private User someCurrentUser;
  @Mock
  private PortfolioAssembler somePortfolioAssembler;

  private PortfolioService portfolioService;

  @Before
  public void setupPortfolioService() {
    portfolioService = new PortfolioService(someCurrentUserSession, somePortfolioAssembler);

    given(someCurrentUserSession.getCurrentUser()).willReturn(someCurrentUser);
  }

  @Test
  public void whenGetPortfolio_thenPortfolioOfCurrentUserIsGot() {
    portfolioService.getPortfolio();

    verify(someCurrentUser).getPortfolio();
  }

  @Test
  public void whenGetPortfolio_thenPortfolioIsConvertedUsingAssembler() throws InvalidStockInPortfolioException {
    portfolioService.getPortfolio();

    verify(somePortfolioAssembler).toDto(any());
  }
}

package ca.ulaval.glo4003.ws.api.portfolio;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.date.DateService;
import ca.ulaval.glo4003.service.portfolio.PortfolioService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioResourceTest {
  @Mock
  private PortfolioService portfolioService;
  @Mock
  private ApiPortfolioAssembler apiPortfolioAssembler;
  @Mock
  private ApiPortfolioReportAssembler apiPortfolioReportAssembler;
  @Mock
  private DateService dateService;
  private PortfolioResource portfolioResource;

  @Before
  public void setupPortfolioResource() {
    portfolioResource = new PortfolioResourceImpl(portfolioService, apiPortfolioAssembler, apiPortfolioReportAssembler, dateService);
  }

  @Test
  public void whenGetPortfolio_thenCallGetPortfolio() {
    portfolioResource.getPortfolio();

    verify(portfolioService).getPortfolio();
  }

  @Test
  public void whenGetPortfolioReport_thenCallGetPortfolioReport() {
    portfolioResource.getPortfolio();

    verify(portfolioService).getPortfolioReport(any());
  }
}

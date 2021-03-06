package ca.ulaval.glo4003.ws.api.portfolio;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.service.portfolio.PortfolioService;
import ca.ulaval.glo4003.ws.api.portfolio.assembler.ApiPortfolioAssembler;
import ca.ulaval.glo4003.ws.api.portfolio.assembler.ApiPortfolioReportAssembler;
import ca.ulaval.glo4003.ws.api.util.SinceParameterConverter;
import javax.ws.rs.BadRequestException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioResourceTest {
  private final String SOME_SINCE_PARAMETER = "LAST_FIVE_DAYS";
  private final SinceParameterConverter sinceParameterConverter = new SinceParameterConverter();
  @Mock
  private PortfolioService portfolioService;
  @Mock
  private ApiPortfolioAssembler apiPortfolioAssembler;
  @Mock
  private ApiPortfolioReportAssembler apiPortfolioReportAssembler;
  private PortfolioResource portfolioResource;


  @Before
  public void setupPortfolioResource() {
    portfolioResource = new PortfolioResource(portfolioService, apiPortfolioAssembler, apiPortfolioReportAssembler, sinceParameterConverter);
  }

  @Test
  public void whenGetPortfolio_thenCallGetPortfolio() {
    portfolioResource.getPortfolio();

    verify(portfolioService).getPortfolio();
  }

  @Test
  public void whenGetPortfolioReport_thenCallGetPortfolioReport() {
    portfolioResource.getPortfolioReport(SOME_SINCE_PARAMETER);

    verify(portfolioService).getPortfolioReport(any());
  }

  @Test
  public void givenInvalidSinceParameter_whenGetPortfolioReport_thenThrowException() {
    ThrowingCallable getPortfolioReport = () -> portfolioResource.getPortfolioReport("invalid");

    assertThatThrownBy(getPortfolioReport).isInstanceOf(BadRequestException.class);
  }

  @Test
  public void givenNullSinceParameter_whenGetPortfolioReport_thenThrowException() {
    ThrowingCallable getPortfolioReport = () -> portfolioResource.getPortfolioReport(null);

    assertThatThrownBy(getPortfolioReport).isInstanceOf(BadRequestException.class);
  }
}

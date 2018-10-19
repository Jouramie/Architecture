package ca.ulaval.glo4003.service.portfolio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;

import ca.ulaval.glo4003.domain.portfolio.Portfolio;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.ws.api.portfolio.PortfolioResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortfolioAssemblerTest {
  private static final String SOME_TITLE = "title";
  private static final int SOME_QUANTITY = 45;

  @Mock
  private Portfolio somePortfolio;

  private PortfolioAssembler portfolioAssembler;

  @Before
  public void setupPortfolioAssembler() {
    portfolioAssembler = new PortfolioAssembler();
  }

  @Test
  public void givenEmptyPortfolio_whenToDto_thenPortfolioItemListIsEmpty() {
    PortfolioResponseDto responseDto = portfolioAssembler.toDto(somePortfolio);

    assertThat(responseDto.stocks).isEmpty();
  }

  @Test
  public void whenToDto_thenPortfolioItemListIsCreated() {
    StockCollection stockCollection = new StockCollection().add(SOME_TITLE, SOME_QUANTITY);
    willReturn(stockCollection).given(somePortfolio).getStocks();

    PortfolioResponseDto responseDto = portfolioAssembler.toDto(somePortfolio);

    int numberOfStocks = somePortfolio.getStocks().getTitles().size();
    assertThat(responseDto.stocks).hasSize(numberOfStocks);
  }
}
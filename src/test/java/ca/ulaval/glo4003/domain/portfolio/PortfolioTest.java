package ca.ulaval.glo4003.domain.portfolio;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class PortfolioTest {
  private final String SOME_TITLE = "MSFT";
  private final int SOME_QUANTITY = 3;

  private Portfolio portfolio;

  @Before
  public void setupPortfolio() {
    portfolio = new Portfolio();
  }

  @Test
  public void whenAddStockToPortfolio_thenItCanBeRetrieved() {
    portfolio.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(portfolio.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }
}

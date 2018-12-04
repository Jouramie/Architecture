package ca.ulaval.glo4003.it.infrastructure.csv;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.market.MarketCsvLoader;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryMarketRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarketCsvLoaderTest {
  private MarketRepository marketRepository;
  @Mock
  private StockRepository stockRepository;

  private MarketCsvLoader loader;

  @Before
  public void setupMarketCSVLoader() {
    marketRepository = new InMemoryMarketRepository();
    loader = new MarketCsvLoader(marketRepository, stockRepository);
  }


  @Test
  public void whenLoad_thenLoadTheValuesFromTheCsvFile() throws Exception {
    loader.load();

    assertThat(marketRepository.findAll()).hasSize(7);
    Market randomMarket = marketRepository.findById(new MarketId("New York"));
    assertThat(randomMarket.getId()).isEqualTo(new MarketId("New York"));
    assertThat(randomMarket.getCurrency().getName()).isEqualTo("USD");
  }
}

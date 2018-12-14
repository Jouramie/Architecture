package ca.ulaval.glo4003.infrastructure.market;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.state.Market;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarketCsvLoaderTest {
  private static final Path TEST_BASE_PATH = Paths.get("src", "test", "data");

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
    loader.load(TEST_BASE_PATH);

    assertThat(marketRepository.findAll()).hasSize(2);
    Market nasdaqMarket = marketRepository.findById(new MarketId("NASDAQ"));
    assertThat(nasdaqMarket.getId()).isEqualTo(new MarketId("NASDAQ"));
    assertThat(nasdaqMarket.getCurrency().getName()).isEqualTo("USD");
  }
}

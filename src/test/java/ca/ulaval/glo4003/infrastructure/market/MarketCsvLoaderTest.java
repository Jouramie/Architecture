package ca.ulaval.glo4003.infrastructure.market;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.io.IOException;
import java.time.LocalTime;
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
  @Mock
  private StockValueRetriever stockValueRetriever;

  private MarketCsvLoader loader;

  @Before
  public void setupMarketCSVLoader() {
    marketRepository = new MarketRepositoryInMemory();
    loader = new MarketCsvLoader(marketRepository, stockRepository, stockValueRetriever);
  }

  @Test
  public void whenLoad_thenLoadTheValuesFromTheCsvFile() throws IOException {
    loader.load();

    assertThat(marketRepository.getAll()).hasSize(7);
    Market randomMarket = marketRepository.getById(new MarketId("New York"));
    assertThat(randomMarket.getId()).isEqualTo(new MarketId("New York"));
    assertThat(randomMarket.getOpeningTime()).isEqualTo(LocalTime.of(14, 30, 0));
    assertThat(randomMarket.getClosingTime()).isEqualTo(LocalTime.of(21, 0, 0));
  }
}

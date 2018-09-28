package ca.ulaval.glo4003.infrastructure.stock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class StockCsvLoaderTest {
  private StockRepository stockRepository;

  private StockCsvLoader loader;

  @Before
  public void setupStockCSVLoader() {
    stockRepository = new StockRepositoryInMemory();
    loader = new StockCsvLoader(stockRepository);
  }

  @Test
  public void whenLoad_thenLoadTheValuesFromTheCsvFile() throws IOException {
    loader.load();

    assertThat(stockRepository.getAll()).hasSize(35);
    Stock randomStock = stockRepository.getByTitle("MSFT");
    assertThat(randomStock.getTitle()).isEqualTo("MSFT");
    assertThat(randomStock.getName()).isEqualTo("Microsoft");
    assertThat(randomStock.getMarketId().getValue()).isEqualTo("Nasdaq");
  }
}

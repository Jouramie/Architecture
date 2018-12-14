package ca.ulaval.glo4003.infrastructure.stock;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.exception.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.state.Market;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.exception.StockNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockCsvLoaderTest {
  private static final Path TEST_BASE_PATH = Paths.get("src", "test", "data");
  private static final LocalDate TEST_STOCK_DATA_DATE = LocalDate.of(2018, 11, 16);

  private final Currency SOME_CURRENCY = Currency.USD;
  @Mock
  private Market someMarket;
  @Mock
  private MarketRepository marketRepository;
  private StockRepository stockRepository;

  private StockCsvLoader loader;

  @Before
  public void setupStockCSVLoader() throws MarketNotFoundException {
    given(someMarket.getCurrency()).willReturn(SOME_CURRENCY);
    given(marketRepository.findById(any())).willReturn(someMarket);
    stockRepository = new InMemoryStockRepository();
    loader = new StockCsvLoader(stockRepository, marketRepository);
  }

  @Test
  public void whenLoad_thenStockRepositoryIsFilledWithStocks()
      throws IOException, MarketNotFoundException {
    loadStocks();

    assertThat(stockRepository.findAll()).hasSize(2);
  }

  @Test
  public void whenLoad_thenLoadTheValuesFromTheCsvFile()
      throws StockNotFoundException, IOException, MarketNotFoundException {
    loadStocks();

    Stock msftStock = stockRepository.findByTitle("MSFT");
    assertThat(msftStock.getTitle()).isEqualTo("MSFT");
    assertThat(msftStock.getName()).isEqualTo("Microsoft");
    assertThat(msftStock.getCategory()).isEqualTo("Technologies");
    assertThat(msftStock.getMarketId().getValue()).isEqualTo("Nasdaq");
  }

  @Test
  public void whenLoad_thenStockStartValueHasCurrencyOfTheMarket()
      throws StockNotFoundException, IOException, MarketNotFoundException {
    loadStocks();

    Stock msftStock = stockRepository.findByTitle("MSFT");
    assertThat(msftStock.getCurrentValue().getCurrency()).isEqualTo(SOME_CURRENCY);
  }

  @Test
  public void whenLoad_thenStockHasLastOpenAndCloseValues()
      throws StockNotFoundException, IOException, MarketNotFoundException {
    loadStocks();

    Stock msftStock = stockRepository.findByTitle("MSFT");
    assertThat(msftStock.getOpenValue().getAmount().doubleValue()).isEqualTo(107.08);
    assertThat(msftStock.getCurrentValue().getAmount().doubleValue()).isEqualTo(108.29);
    assertThat(msftStock.getTodayMaximumValue().getAmount().doubleValue()).isEqualTo(108.88);
    assertThat(msftStock.getValueHistory().getAllStoredValues()).hasSize(2);
  }

  @Test
  public void whenLoad_thenAllStocksHaveTheSameLastDate() throws IOException, MarketNotFoundException {
    loadStocks();

    List<LocalDate> latestDates = stockRepository.findAll().stream().map((stock) ->
        stock.getValueHistory().getLatestHistoricalValue().date)
        .collect(toList());

    assertThat(latestDates).containsOnly(latestDates.get(0));
  }

  private void loadStocks() throws IOException, MarketNotFoundException {
    loader.load(TEST_BASE_PATH, TEST_STOCK_DATA_DATE);
  }
}

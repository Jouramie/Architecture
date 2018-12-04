package ca.ulaval.glo4003.infrastructure;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryStockRepository;
import ca.ulaval.glo4003.infrastructure.stock.StockCsvLoader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockCsvLoaderTest {
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
    loader.load();

    assertThat(stockRepository.findAll()).isNotEmpty();
  }

  @Test
  public void whenLoad_thenLoadTheValuesFromTheCsvFile()
      throws StockNotFoundException, IOException, MarketNotFoundException {
    loader.load();

    Stock msftStock = stockRepository.findByTitle("MSFT");
    assertThat(msftStock.getTitle()).isEqualTo("MSFT");
    assertThat(msftStock.getName()).isEqualTo("Microsoft");
    assertThat(msftStock.getCategory()).isEqualTo("Technologies");
    assertThat(msftStock.getMarketId().getValue()).isEqualTo("Nasdaq");
  }

  @Test
  public void whenLoad_thenStockStartValueHasCurrencyOfTheMarket()
      throws StockNotFoundException, IOException, MarketNotFoundException {
    loader.load();

    Stock mmmStock = stockRepository.findByTitle("MMM");
    assertThat(mmmStock.getCurrentValue().getCurrency()).isEqualTo(SOME_CURRENCY);
  }

  @Test
  public void whenLoad_thenStockHasLastOpenAndCloseValues()
      throws StockNotFoundException, IOException, MarketNotFoundException {
    loader.load();

    Stock msftStock = stockRepository.findByTitle("MSFT");
    assertThat(msftStock.getOpenValue().getAmount().doubleValue()).isEqualTo(107.08);
    assertThat(msftStock.getCurrentValue().getAmount().doubleValue()).isEqualTo(108.29);
    assertThat(msftStock.getTodayMaximumValue().getAmount().doubleValue()).isEqualTo(108.88);
    assertThat(msftStock.getValueHistory().getAllStoredValues()).hasSize(5255);
  }

  @Test
  public void whenLoad_thenAllStocksHaveTheSameLastDate() throws IOException, MarketNotFoundException {
    loader.load();

    List<LocalDate> latestDates = stockRepository.findAll().stream().map((stock) ->
        stock.getValueHistory().getLatestHistoricalValue().date)
        .collect(toList());

    assertThat(latestDates).containsOnly(latestDates.get(0));
  }
}

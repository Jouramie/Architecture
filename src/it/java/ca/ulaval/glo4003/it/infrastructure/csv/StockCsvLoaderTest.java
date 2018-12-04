package ca.ulaval.glo4003.it.infrastructure.csv;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import ca.ulaval.glo4003.domain.stock.query.StockQueryBuilder;
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

    StockQuery emptyQuery = new StockQueryBuilder().build();
    assertThat(stockRepository.find(emptyQuery)).isNotEmpty();
  }

  @Test
  public void whenLoad_thenLoadTheValuesFromTheCsvFile()
      throws IOException, MarketNotFoundException {
    loader.load();

    StockQuery stockQuery = new StockQueryBuilder().withTitle("MSFT").build();
    Stock msftStock = stockRepository.find(stockQuery).get(0);
    assertThat(msftStock.getTitle()).isEqualTo("MSFT");
    assertThat(msftStock.getName()).isEqualTo("Microsoft");
    assertThat(msftStock.getCategory()).isEqualTo("Technologies");
    assertThat(msftStock.getMarketId().getValue()).isEqualTo("Nasdaq");
  }

  @Test
  public void whenLoad_thenStockStartValueHasCurrencyOfTheMarket()
      throws IOException, MarketNotFoundException {
    loader.load();

    StockQuery stockQuery = new StockQueryBuilder().withTitle("MMM").build();
    Stock mmmStock = stockRepository.find(stockQuery).get(0);
    assertThat(mmmStock.getValue().getLatestValue().getCurrency()).isEqualTo(SOME_CURRENCY);
  }

  @Test
  public void whenLoad_thenStockHasLastOpenAndCloseValues()
      throws IOException, MarketNotFoundException {
    loader.load();

    StockQuery stockQuery = new StockQueryBuilder().withTitle("MSFT").build();
    Stock msftStock = stockRepository.find(stockQuery).get(0);
    assertThat(msftStock.getValue().getOpenValue().getAmount().doubleValue()).isEqualTo(107.08);
    assertThat(msftStock.getValue().getLatestValue().getAmount().doubleValue()).isEqualTo(108.29);
    assertThat(msftStock.getValue().getMaximumValue().getAmount().doubleValue()).isEqualTo(108.88);
    assertThat(msftStock.getValueHistory().getAllStoredValues()).hasSize(5255);
  }

  @Test
  public void whenLoad_thenAllStocksHaveTheSameLastDate() throws IOException, MarketNotFoundException {
    loader.load();
    StockQuery emptyQuery = new StockQueryBuilder().build();
    List<LocalDate> latestDates = stockRepository.find(emptyQuery).stream().map((stock) ->
        stock.getValueHistory().getLatestValue().date)
        .collect(toList());

    assertThat(latestDates).containsOnly(latestDates.get(0));
  }
}

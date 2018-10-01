package ca.ulaval.glo4003.infrastructure.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StockCsvLoaderTest {
  private final Currency SOME_CURRENCY = new Currency("USD", new BigDecimal(1.0));
  @Mock
  private Market someMarket;
  @Mock
  private MarketRepository marketRepository;
  private StockRepository stockRepository;

  private StockCsvLoader loader;

  @Before
  public void setupStockCSVLoader() {
    given(someMarket.getCurrency()).willReturn(SOME_CURRENCY);
    given(marketRepository.getById(any())).willReturn(someMarket);
    stockRepository = new StockRepositoryInMemory();
    loader = new StockCsvLoader(stockRepository, marketRepository);
  }

  @Test
  public void whenLoad_thenLoadTheValuesFromTheCsvFile() throws IOException {
    loader.load();

    assertThat(stockRepository.getAll()).hasSize(35);
    Stock randomStock = stockRepository.getByTitle("MSFT");
    assertThat(randomStock.getTitle()).isEqualTo("MSFT");
    assertThat(randomStock.getName()).isEqualTo("Microsoft");
    assertThat(randomStock.getCategory()).isEqualTo("Technologies");
    assertThat(randomStock.getMarketId().getValue()).isEqualTo("Nasdaq");
  }

  @Test
  public void whenLoad_thenStockStartValueHasCurrencyOfTheMarket() throws IOException {
    loader.load();

    Stock randomStock = stockRepository.getByTitle("MMM");
    assertThat(randomStock.getValue().getCurrentValue().getCurrency()).isEqualTo(SOME_CURRENCY);
  }

  @Test
  public void whenLoad_thenStockHasLastOpenAndCloseValues() throws IOException {
    loader.load();

    Stock randomStock = stockRepository.getByTitle("MSFT");
    assertThat(randomStock.getValue().getOpenValue().getAmount().doubleValue()).isEqualTo(114.19);
    assertThat(randomStock.getValue().getCloseValue().getAmount().doubleValue()).isEqualTo(114.37);
  }
}

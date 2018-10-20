package ca.ulaval.glo4003.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.persistence.InMemoryStockRepository;
import ca.ulaval.glo4003.infrastructure.stock.StockCsvLoader;
import java.io.IOException;
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
  public void whenLoad_thenLoadTheValuesFromTheCsvFile()
      throws StockNotFoundException, IOException, MarketNotFoundException {
    loader.load();

    assertThat(stockRepository.findAll()).hasSize(35);
    Stock randomStock = stockRepository.findByTitle("MSFT");
    assertThat(randomStock.getTitle()).isEqualTo("MSFT");
    assertThat(randomStock.getName()).isEqualTo("Microsoft");
    assertThat(randomStock.getCategory()).isEqualTo("Technologies");
    assertThat(randomStock.getMarketId().getValue()).isEqualTo("Nasdaq");
  }

  @Test
  public void whenLoad_thenStockStartValueHasCurrencyOfTheMarket()
      throws StockNotFoundException, IOException, MarketNotFoundException {
    loader.load();

    Stock randomStock = stockRepository.findByTitle("MMM");
    assertThat(randomStock.getValue().getCurrentValue().getCurrency()).isEqualTo(SOME_CURRENCY);
  }

  @Test
  public void whenLoad_thenStockHasLastOpenAndCloseValues()
      throws StockNotFoundException, IOException, MarketNotFoundException {
    loader.load();

    Stock randomStock = stockRepository.findByTitle("MSFT");
    assertThat(randomStock.getValue().getOpenValue().getAmount().doubleValue()).isEqualTo(112.63);
    assertThat(randomStock.getValue().getCloseValue().getAmount().doubleValue()).isEqualTo(112.13);
    assertThat(randomStock.getValue().getMaximumValue().getAmount().doubleValue()).isEqualTo(113.17);
    assertThat(randomStock.getValueHistory().getAllStoredValues()).hasSize(5984);
  }
}

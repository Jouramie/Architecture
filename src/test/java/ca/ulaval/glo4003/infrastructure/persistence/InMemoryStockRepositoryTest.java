package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class InMemoryStockRepositoryTest {
  private final MarketId SOME_MARKET_ID = new MarketId("NASDAQ");
  private final MoneyAmount SOME_START_AMOUNT = new MoneyAmount(12.34, new Currency("CAD", new BigDecimal(0.77)));
  private final Stock SOME_STOCK = new Stock("STO1", "Stock 1", "Banking", SOME_MARKET_ID, SOME_START_AMOUNT, SOME_START_AMOUNT);
  private final Stock SOME_OTHER_STOCK = new Stock("STO2", "Stock 2", "Banking", SOME_MARKET_ID, SOME_START_AMOUNT, SOME_START_AMOUNT);
  private final Stock SOME_STOCK_IN_DIFFERENT_MARKET = new Stock("STO3", "Stock 3", "Banking", new MarketId("TMX"), SOME_START_AMOUNT, SOME_START_AMOUNT);

  private InMemoryStockRepository repository;

  @Before
  public void setupStockRepository() {
    repository = new InMemoryStockRepository();
    repository.add(SOME_STOCK);
    repository.add(SOME_OTHER_STOCK);
    repository.add(SOME_STOCK_IN_DIFFERENT_MARKET);
  }

  @Test
  public void whenGetByTitleAnExistingStock_thenStockIsReturned() throws StockNotFoundException {
    Stock result = repository.getByTitle(SOME_STOCK.getTitle());

    assertThat(result).isEqualTo(SOME_STOCK);
  }

  @Test
  public void whenGetByTitleANonExistingStock_thenStockNotFoundExceptionIsThrown() {
    assertThatExceptionOfType(StockNotFoundException.class).isThrownBy(() -> repository.getByTitle("ASDF"));
  }

  @Test
  public void whenGetByNameAnExistingStock_thenStockIsReturned() throws StockNotFoundException {
    Stock result = repository.getByName(SOME_STOCK.getName());

    assertThat(result).isEqualTo(SOME_STOCK);
  }

  @Test
  public void whenGetByNameANonExistingStock_thenStockNotFoundExceptionIsThrown() {
    assertThatExceptionOfType(StockNotFoundException.class).isThrownBy(() -> repository.getByName("ASDF"));
  }

  @Test
  public void whenGetAll_thenReturnAllStocks() {
    List<Stock> result = repository.getAll();

    assertThat(result).containsExactlyInAnyOrder(SOME_STOCK, SOME_OTHER_STOCK, SOME_STOCK_IN_DIFFERENT_MARKET);
  }

  @Test
  public void whenGetAllByMarket_thenReturnAllStocksOfMarket() {
    List<Stock> result = repository.getByMarket(SOME_MARKET_ID);

    assertThat(result).containsExactlyInAnyOrder(SOME_STOCK, SOME_OTHER_STOCK);
  }
}

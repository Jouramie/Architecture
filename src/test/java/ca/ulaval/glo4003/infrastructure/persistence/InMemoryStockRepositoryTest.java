package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.util.StockBuilder;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class InMemoryStockRepositoryTest {
  private final MarketId SOME_MARKET_ID = new MarketId("NASDAQ");
  private final Stock SOME_STOCK = new StockBuilder().withTitle("STO1").withName("Stock 1").withMarketId(SOME_MARKET_ID).build();
  private final Stock SOME_OTHER_STOCK = new StockBuilder().withTitle("STO2").withName("Stock 2").withMarketId(SOME_MARKET_ID).build();
  private final Stock SOME_STOCK_IN_DIFFERENT_MARKET = new StockBuilder().withTitle("STO3").withName("Stock 3").withMarketId(new MarketId("TSX")).build();

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
    Stock result = repository.findByTitle(SOME_STOCK.getTitle());

    assertThat(result).isEqualTo(SOME_STOCK);
  }

  @Test
  public void whenGetByTitleANonExistingStock_thenStockNotFoundExceptionIsThrown() {
    assertThatExceptionOfType(StockNotFoundException.class).isThrownBy(() -> repository.findByTitle("ASDF"));
  }

  @Test
  public void whenGetByNameAnExistingStock_thenStockIsReturned() throws StockNotFoundException {
    Stock result = repository.findByName(SOME_STOCK.getName());

    assertThat(result).isEqualTo(SOME_STOCK);
  }

  @Test
  public void whenGetByNameANonExistingStock_thenStockNotFoundExceptionIsThrown() {
    assertThatExceptionOfType(StockNotFoundException.class).isThrownBy(() -> repository.findByName("ASDF"));
  }

  @Test
  public void whenGetAll_thenReturnAllStocks() {
    List<Stock> result = repository.findAll();

    assertThat(result).containsExactlyInAnyOrder(SOME_STOCK, SOME_OTHER_STOCK, SOME_STOCK_IN_DIFFERENT_MARKET);
  }

  @Test
  public void whenGetAllByMarket_thenReturnAllStocksOfMarket() {
    List<Stock> result = repository.findByMarket(SOME_MARKET_ID);

    assertThat(result).containsExactlyInAnyOrder(SOME_STOCK, SOME_OTHER_STOCK);
  }
}

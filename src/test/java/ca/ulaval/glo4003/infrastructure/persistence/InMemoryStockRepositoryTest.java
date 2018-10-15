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
  private static final String BANKING_CATEGORY = "Banking";
  private static final String MEDIA_CATEGORY = "Media";

  private final Stock SOME_STOCK = new StockBuilder().withTitle("ST01").withName("Stock 1").build();
  private final Stock SOME_OTHER_STOCK = new StockBuilder().withTitle("STO2").withName("Stock 2")
      .withCategory(BANKING_CATEGORY).build();
  private final Stock SOME_STOCK_IN_DIFFERENT_MARKET = new StockBuilder().withTitle("STO3")
      .withName("Stock 3").withMarketId(new MarketId("TMX")).build();
  private final Stock SOME_MEDIA_STOCK = new StockBuilder().withTitle("STO4").withName("Stock 4")
      .withCategory(MEDIA_CATEGORY).build();

  private InMemoryStockRepository repository;

  @Before
  public void setupStockRepository() {
    repository = new InMemoryStockRepository();
    repository.add(SOME_STOCK);
    repository.add(SOME_OTHER_STOCK);
    repository.add(SOME_STOCK_IN_DIFFERENT_MARKET);
    repository.add(SOME_MEDIA_STOCK);
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

    assertThat(result).containsExactlyInAnyOrder(SOME_STOCK, SOME_OTHER_STOCK,
        SOME_STOCK_IN_DIFFERENT_MARKET, SOME_MEDIA_STOCK);
  }

  @Test
  public void whenGetAllByMarket_thenReturnAllStocksOfMarket() {
    List<Stock> result = repository.getByMarket(StockBuilder.DEFAULT_MARKET_ID);

    assertThat(result).containsExactlyInAnyOrder(SOME_STOCK, SOME_OTHER_STOCK, SOME_MEDIA_STOCK);
  }

  @Test
  public void whenGetCategories_thenReturnAllCategories() {
    List<String> result = repository.getCategories();

    assertThat(result).containsExactlyInAnyOrder(BANKING_CATEGORY, MEDIA_CATEGORY, StockBuilder.DEFAULT_CATEGORY);
  }
}

package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class InMemoryStockRepositoryTest {
  private static final String BANKING_CATEGORY = "Banking";
  private static final String MEDIA_CATEGORY = "Media";
  private static final String GREEN_TECHNOLOGY_CATEGORY = "Green Technology";
  private static final MarketId SOME_MARKET_ID = new MarketId("NASDAQ");

  private final Stock SOME_NASDAQ_BANKING_STOCK = new TestStockBuilder().withTitle("SNBS")
      .withName("NASDAQ banking stock").withMarketId(SOME_MARKET_ID).withCategory(BANKING_CATEGORY)
      .build();
  private final Stock SOME_NASDAQ_GREEN_TECH_STOCK = new TestStockBuilder().withTitle("SNQTS")
      .withName("NASDAQ green tech stock").withMarketId(SOME_MARKET_ID)
      .withCategory(GREEN_TECHNOLOGY_CATEGORY).build();
  private final Stock SOME_TSX_BANKING_STOCK = new TestStockBuilder().withTitle("STNS")
      .withName("TSX banking stock").withMarketId(new MarketId("TSX"))
      .withCategory(BANKING_CATEGORY).build();
  private final Stock SOME_NASDAQ_MEDIA_STOCK = new TestStockBuilder().withTitle("SNMS")
      .withName("NASDAQ media stock").withMarketId(SOME_MARKET_ID).withCategory(MEDIA_CATEGORY)
      .build();

  private InMemoryStockRepository repository;

  @Before
  public void setupStockRepository() {
    repository = new InMemoryStockRepository();
    repository.add(SOME_NASDAQ_BANKING_STOCK);
    repository.add(SOME_NASDAQ_GREEN_TECH_STOCK);
    repository.add(SOME_TSX_BANKING_STOCK);
    repository.add(SOME_NASDAQ_MEDIA_STOCK);
  }

  @Test
  public void whenGetByTitleAnExistingStock_thenStockIsReturned() throws StockNotFoundException {
    Stock result = repository.findByTitle(SOME_NASDAQ_BANKING_STOCK.getTitle());

    assertThat(result).isEqualTo(SOME_NASDAQ_BANKING_STOCK);
  }

  @Test
  public void whenGetByTitleANonExistingStock_thenStockNotFoundExceptionIsThrown() {
    assertThatExceptionOfType(StockNotFoundException.class).isThrownBy(() -> repository.findByTitle("ASDF"));
  }

  @Test
  public void givenExistingStockName_whenQueryStock_thenStockIsReturned() {
    String existingStockName = SOME_NASDAQ_BANKING_STOCK.getName();

    List<Stock> result = repository.queryStocks(existingStockName, null);

    assertThat(result).containsOnly(SOME_NASDAQ_BANKING_STOCK);
  }

  @Test
  public void givenWrongName_whenQueryStock_thenEmptyListIsReturned() {
    String wrongName = "wrong";

    List<Stock> result = repository.queryStocks(wrongName, null);

    assertThat(result).isEmpty();
  }

  @Test
  public void givenACategory_whenQueryStock_thenAllStocksWithTheCategoryAreReturned() {
    List<Stock> result = repository.queryStocks(null, BANKING_CATEGORY);

    assertThat(result).containsExactlyInAnyOrder(SOME_NASDAQ_BANKING_STOCK, SOME_TSX_BANKING_STOCK);
  }

  @Test
  public void givenWrongCategory_whenQueryStock_thenAnEmptyListIsReturned() {
    String wrongCategory = "wrong";

    List<Stock> result = repository.queryStocks(null, wrongCategory);

    assertThat(result).isEmpty();
  }

  @Test
  public void givenANameAndACategory_whenQueryStock_thenStockWithTheNameAndTheCategoryIsReturned() {
    String existingStockName = SOME_NASDAQ_BANKING_STOCK.getName();

    List<Stock> result = repository.queryStocks(existingStockName, BANKING_CATEGORY);

    assertThat(result).containsOnly(SOME_NASDAQ_BANKING_STOCK);
  }

  @Test
  public void whenGetAll_thenReturnAllStocks() {
    List<Stock> result = repository.findAll();

    assertThat(result).containsExactlyInAnyOrder(SOME_NASDAQ_BANKING_STOCK,
        SOME_NASDAQ_GREEN_TECH_STOCK, SOME_TSX_BANKING_STOCK, SOME_NASDAQ_MEDIA_STOCK);
  }

  @Test
  public void whenGetAllByMarket_thenReturnAllStocksOfMarket() {
    List<Stock> result = repository.findByMarket(TestStockBuilder.DEFAULT_MARKET_ID);

    assertThat(result).containsExactlyInAnyOrder(SOME_NASDAQ_BANKING_STOCK,
        SOME_NASDAQ_GREEN_TECH_STOCK, SOME_NASDAQ_MEDIA_STOCK);
  }

  @Test
  public void whenGetCategories_thenReturnAllCategories() {
    List<String> result = repository.getCategories();

    assertThat(result).containsExactlyInAnyOrder(BANKING_CATEGORY, MEDIA_CATEGORY,
        GREEN_TECHNOLOGY_CATEGORY);
  }
}

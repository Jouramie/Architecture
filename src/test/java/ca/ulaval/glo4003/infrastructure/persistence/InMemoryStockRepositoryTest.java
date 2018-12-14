package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.exception.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.query.StockQueryByNameAndCategory;
import ca.ulaval.glo4003.infrastructure.stock.InMemoryStockRepository;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
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
  public void whenFindingByTitle_thenStockIsReturned() throws StockNotFoundException {
    Stock result = repository.findByTitle(SOME_NASDAQ_BANKING_STOCK.getTitle());

    assertThat(result).isEqualTo(SOME_NASDAQ_BANKING_STOCK);
  }

  @Test
  public void givenStockDoesNotExist_whenFindingByTitle_thenStockNotFoundExceptionIsThrown() {
    assertThatExceptionOfType(StockNotFoundException.class)
        .isThrownBy(() -> repository.findByTitle("ASDF"));
  }

  @Test
  public void whenFindingByTitles_thenStocksAreReturned() throws StockNotFoundException {
    List<String> stockTitles = Arrays.asList(SOME_NASDAQ_BANKING_STOCK.getTitle(), SOME_TSX_BANKING_STOCK.getTitle());

    List<Stock> stocks = repository.findByTitles(stockTitles);

    assertThat(stocks).containsExactly(SOME_NASDAQ_BANKING_STOCK, SOME_TSX_BANKING_STOCK);
  }

  @Test
  public void givenStockDoesNotExist_whenFindingByTitles_thenStockNotFoundExceptionIsThrown() {
    List<String> stockTitles = Arrays.asList(SOME_NASDAQ_BANKING_STOCK.getTitle(), "invalid");

    ThrowingCallable findByTitles = () -> repository.findByTitles(stockTitles);

    assertThatThrownBy(findByTitles).isInstanceOf(StockNotFoundException.class);
  }

  @Test
  public void whenQueryStocks_thenQueryTestIsCalledWithEachStock() {
    StockQueryByNameAndCategory stockQuery = mock(StockQueryByNameAndCategory.class);

    repository.queryStocks(stockQuery);

    repository.findAll().forEach((stock) -> verify(stockQuery).test(stock));
  }

  @Test
  public void whenFindingAllStocks_thenReturnAllStocks() {
    List<Stock> result = repository.findAll();

    assertThat(result).containsExactlyInAnyOrder(SOME_NASDAQ_BANKING_STOCK,
        SOME_NASDAQ_GREEN_TECH_STOCK, SOME_TSX_BANKING_STOCK, SOME_NASDAQ_MEDIA_STOCK);
  }

  @Test
  public void whenFindingAllByMarket_thenReturnAllStocksOfMarket() {
    List<Stock> result = repository.findByMarket(TestStockBuilder.DEFAULT_MARKET_ID);

    assertThat(result).containsExactlyInAnyOrder(SOME_NASDAQ_BANKING_STOCK,
        SOME_NASDAQ_GREEN_TECH_STOCK, SOME_NASDAQ_MEDIA_STOCK);
  }

  @Test
  public void whenFindingAllCategories_thenReturnAllCategories() {
    List<String> result = repository.findAllCategories();

    assertThat(result).containsExactlyInAnyOrder(BANKING_CATEGORY, MEDIA_CATEGORY,
        GREEN_TECHNOLOGY_CATEGORY);
  }
}

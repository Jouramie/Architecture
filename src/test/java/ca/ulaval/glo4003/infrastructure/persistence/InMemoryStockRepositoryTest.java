package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

  @Mock
  private StockQuery someStockQuery;

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
  public void whenFinding_stockQueryIsAskedForMatchingStocks() {
    repository.find(someStockQuery);

    verify(someStockQuery).getMatchingStocks(any());
  }

  @Test
  public void whenFindingAllCategories_thenReturnAllCategories() {
    List<String> result = repository.findAllCategories();

    assertThat(result).containsExactlyInAnyOrder(BANKING_CATEGORY, MEDIA_CATEGORY,
        GREEN_TECHNOLOGY_CATEGORY);
  }
}

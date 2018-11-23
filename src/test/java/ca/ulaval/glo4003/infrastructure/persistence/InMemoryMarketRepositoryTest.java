package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketNotFoundForStockException;
import ca.ulaval.glo4003.domain.market.TestingMarketBuilder;
import ca.ulaval.glo4003.domain.market.states.Market;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;

public class InMemoryMarketRepositoryTest {
  private final MarketId SOME_MARKET_ID = new MarketId("NASDAQ");
  private final Market SOME_MARKET = new TestingMarketBuilder().withId(SOME_MARKET_ID).build();
  private final MarketId SOME_OTHER_MARKET_ID = new MarketId("TMX");
  private final Market SOME_OTHER_MARKET = new TestingMarketBuilder().withId(SOME_OTHER_MARKET_ID).build();

  private InMemoryMarketRepository repository;

  @Before
  public void setupMarketRepository() {
    repository = new InMemoryMarketRepository();
    repository.add(SOME_MARKET);
    repository.add(SOME_OTHER_MARKET);
  }

  @Test
  public void whenFindingAllMarkets_thenReturnAllMarkets() {
    List<Market> result = repository.findAll();

    assertThat(result).containsExactlyInAnyOrder(SOME_MARKET, SOME_OTHER_MARKET);
  }

  @Test
  public void whenFindingMarketById_thenMarketIsReturned() throws MarketNotFoundException {
    Market result = repository.findById(SOME_MARKET_ID);

    assertThat(result).isEqualTo(SOME_MARKET);
  }

  @Test
  public void givenMarketDoesNotExist_whenFindingByTitle_thenStockNotFoundExceptionIsThrown() {
    ThrowingCallable findById = () -> repository.findById(new MarketId("ASDF"));

    assertThatThrownBy(findById).isInstanceOf(MarketNotFoundException.class);
  }

  @Test
  public void whenFindingMarketForStock_thenMarketIsReturned() throws MarketNotFoundForStockException {
    Market market = repository.findMarketForStock(TestingMarketBuilder.DEFAULT_STOCK_TITLE);

    assertThat(market).isEqualTo(SOME_MARKET);
  }

  @Test
  public void givenNoMarketContainsStock_whenFindingMarketForStock_thenExceptionIsThrown() {
    ThrowingCallable findMarketStock = () -> repository.findMarketForStock("no market");

    assertThatThrownBy(findMarketStock).isInstanceOf(MarketNotFoundForStockException.class);
  }
}

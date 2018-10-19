package ca.ulaval.glo4003.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class InMemoryMarketRepositoryTest {
  private final MarketId SOME_MARKET_ID = new MarketId("NASDAQ");
  private final Market SOME_MARKET = new Market(SOME_MARKET_ID, null, null, null, null, null);
  private final MarketId SOME_OTHER_MARKET_ID = new MarketId("TMX");
  private final Market SOME_OTHER_MARKET = new Market(SOME_OTHER_MARKET_ID, null, null, null, null, null);

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
    assertThatExceptionOfType(MarketNotFoundException.class)
        .isThrownBy(() -> repository.findById(new MarketId("ASDF")));
  }
}

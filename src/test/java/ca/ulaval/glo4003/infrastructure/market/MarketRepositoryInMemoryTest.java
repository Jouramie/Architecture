package ca.ulaval.glo4003.infrastructure.market;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MarketRepositoryInMemoryTest {
    private final MarketId SOME_MARKET_ID = new MarketId("NASDAQ");
    private final Market SOME_MARKET = new Market(SOME_MARKET_ID, null, null, null, null);
    private final MarketId SOME_OTHER_MARKET_ID = new MarketId("TMX");
    private final Market SOME_OTHER_MARKET = new Market(SOME_OTHER_MARKET_ID, null, null, null, null);

    private MarketRepositoryInMemory repository;

    @Before
    public void setupMarketRepository() {
        repository = new MarketRepositoryInMemory();
        repository.add(SOME_MARKET);
        repository.add(SOME_OTHER_MARKET);
    }

    @Test
    public void whenGetAll_thenReturnAllMarkets() {
        List<Market> result = repository.getAll();

        assertThat(result).containsExactlyInAnyOrder(SOME_MARKET, SOME_OTHER_MARKET);
    }

    @Test
    public void whenGetByIdAnExistingStock_thenMarketIsReturned() {
        Market result = repository.getById(SOME_MARKET_ID);

        assertThat(result).isEqualTo(SOME_MARKET);
    }

    @Test
    public void whenGetByTitleANonExistingStock_thenStockNotFoundExceptionIsThrown() {
        assertThatExceptionOfType(MarketNotFoundException.class).isThrownBy(() -> repository.getById(new MarketId("ASDF")));
    }
}

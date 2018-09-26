package ca.ulaval.glo4003.domain.market;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MarketIdTest {
    private final String SOME_MARKET_NAME = "NASDAQ";
    private final String SOME_OTHER_MARKET_NAME = "TMX";

    private MarketId id;

    @Before
    public void setupMarketId() {
        id = new MarketId(SOME_MARKET_NAME);
    }

    @Test
    public void whenGetValue_thenReturnMarketName() {
        String value = id.getValue();

        assertThat(value).isEqualTo(SOME_MARKET_NAME);
    }

    @Test
    public void givenOtherNullObject_whenEquals_thenReturnFalse() {
        boolean result = id.equals(null);

        assertThat(result).isFalse();
    }

    @Test
    public void givenOtherObjectType_whenEquals_thenReturnFalse() {
        boolean result = id.equals(42);

        assertThat(result).isFalse();
    }

    @Test
    public void givenOtherMarketIdWithDifferentValue_whenEquals_thenReturnFalse() {
        boolean result = id.equals(new MarketId(SOME_OTHER_MARKET_NAME));

        assertThat(result).isFalse();
    }

    @Test
    public void givenOtherIdenticalMarketId_whenEquals_thenReturnTrue() {
        boolean result = id.equals(new MarketId(SOME_MARKET_NAME));

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentMarketId_whenHashCode_thenReturnDifferentHashes() {
        int firstHash = id.hashCode();
        int secondHash = new MarketId(SOME_OTHER_MARKET_NAME).hashCode();

        assertThat(firstHash).isNotEqualTo(secondHash);
    }

    @Test
    public void givenIdenticalMarketId_whenHashCode_thenReturnSameHash() {
        int firstHash = id.hashCode();
        int secondHash = new MarketId(SOME_MARKET_NAME).hashCode();

        assertThat(firstHash).isEqualTo(secondHash);
    }
}

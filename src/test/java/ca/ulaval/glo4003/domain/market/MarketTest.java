package ca.ulaval.glo4003.domain.market;

import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.domain.market.states.OpenMarketState;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class MarketTest {
    private static final MarketId SOME_MARKET_ID = new MarketId("NASDAQ");
    private static final LocalTime SOME_OPENING_TIME = LocalTime.of(14, 30, 0);
    private static final LocalTime SOME_CLOSING_TIME = LocalTime.of(21, 0, 0);
    private static final LocalDateTime SOME_TIME = LocalDateTime.of(2018, 9, 22, 15, 0, 0);
    private final Stock SOME_STOCK = new Stock("STO1", "Stock 1", SOME_MARKET_ID);
    private final Stock SOME_OTHER_STOCK = new Stock("STO2", "Stock 2", SOME_MARKET_ID);

    @Mock
    StockRepository stockRepository;
    @Mock
    StockValueRetriever stockValueRetriever;
    @Mock
    private MarketState state;

    private Market closeMarket;
    private Market openMarket;
    private Market haltedMarket;

    @Before
    public void setupMarkets() {
        closeMarket = new Market(SOME_MARKET_ID, SOME_OPENING_TIME, SOME_CLOSING_TIME, stockRepository, stockValueRetriever);

        openMarket = new Market(SOME_MARKET_ID, SOME_OPENING_TIME, SOME_CLOSING_TIME, stockRepository, stockValueRetriever);
        openMarket.setState(new OpenMarketState());

        haltedMarket = new Market(SOME_MARKET_ID, SOME_OPENING_TIME, SOME_CLOSING_TIME, stockRepository, stockValueRetriever);
        haltedMarket.halt();

        given(stockRepository.getStocksOfMarket(SOME_MARKET_ID)).willReturn(Arrays.asList(SOME_STOCK, SOME_OTHER_STOCK));
    }

    @Test
    public void whenGetId_thenReturnMarketName() {
        MarketId id = openMarket.getId();

        assertThat(id).isEqualTo(SOME_MARKET_ID);
    }

    @Test
    public void whenGetOpeningTime_thenReturnMarketOpeningTime() {
        LocalTime openingTime = openMarket.getOpeningTime();

        assertThat(openingTime).isEqualTo(SOME_OPENING_TIME);
    }

    @Test
    public void whenGetClosingTime_thenReturnMarketClosingTime() {
        LocalTime closingTime = openMarket.getClosingTime();

        assertThat(closingTime).isEqualTo(SOME_CLOSING_TIME);
    }

    @Test
    public void givenNonHaltedMarket_whenIsHalted_thenReturnFalse() {
        boolean isHalted = openMarket.isHalted();

        assertThat(isHalted).isFalse();
    }

    @Test
    public void givenHaltedMarket_whenIsHalted_thenReturnTrue() {
        boolean isHalted = haltedMarket.isHalted();

        assertThat(isHalted).isTrue();
    }

    @Test
    public void givenNonHaltedOpenMarket_whenIsOpen_thenReturnTrue() {
        boolean isOpened = openMarket.isOpen();

        assertThat(isOpened).isTrue();
    }

    @Test
    public void givenNonHaltedClosedMarket_whenIsOpen_thenReturnFalse() {
        boolean isOpened = closeMarket.isOpen();

        assertThat(isOpened).isFalse();
    }

    @Test
    public void givenHaltedOpenMarket_whenIsOpen_thenReturnFalse() {
        boolean isOpened = haltedMarket.isOpen();

        assertThat(isOpened).isFalse();
    }

    @Test
    public void whenUpdate_thenUpdateCurrentState() {
        openMarket.setState(state);

        openMarket.update(SOME_TIME);

        verify(state).update(openMarket, SOME_TIME);
    }

    @Test
    public void whenUpdateAllStockValues_thenRetrieveAllStocksOfMarketAndUpdateThem() {
        openMarket.updateAllStockValues();

        verify(stockValueRetriever, times(2)).updateStockValue(any());
        verify(stockValueRetriever).updateStockValue(SOME_STOCK);
        verify(stockValueRetriever).updateStockValue(SOME_OTHER_STOCK);
    }
}

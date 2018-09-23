package ca.ulaval.glo4003.domain.market.states;

import ca.ulaval.glo4003.domain.market.Market;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OpenMarketStateTest {
    private static final LocalTime SOME_OPENING_TIME = LocalTime.of(14, 30, 0);
    private static final LocalTime SOME_CLOSING_TIME = LocalTime.of(21, 0, 0);
    private static final LocalDateTime SOME_OPEN_TIME = LocalDateTime.of(2018, 9, 22, 15, 0, 0);
    private static final LocalDateTime SOME_CLOSED_TIME = LocalDateTime.of(2018, 9, 22, 21, 0, 0);

    @Mock
    Market market;

    private OpenMarketState state;

    @Before
    public void setupOpenMarketState() {
        state = new OpenMarketState();

        given(market.getOpeningTime()).willReturn(SOME_OPENING_TIME);
        given(market.getClosingTime()).willReturn(SOME_CLOSING_TIME);
    }

    @Test
    public void whenTimeDoesNotCloseTheMarket_thenUpdateAllStockValuesAndStayOpen() {
        state.update(market, SOME_OPEN_TIME);

        verify(market).updateAllStockValues();
        verify(market, never()).setState(any());
    }

    @Test
    public void whenTimeClosesTheMarket_thenUpdateAndCloseAllStockAndChangeStateToClose() {
        state.update(market, SOME_CLOSED_TIME);

        verify(market).updateAllStockValues();
        verify(market).closeAllStocks();
        verify(market).setState(any(CloseMarketState.class));
    }
}

package ca.ulaval.glo4003.domain.market.states;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Matchers.any;

import ca.ulaval.glo4003.domain.market.Market;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CloseMarketStateTest {
  private static final LocalTime SOME_OPENING_TIME = LocalTime.of(14, 30, 0);
  private static final LocalTime SOME_CLOSING_TIME = LocalTime.of(21, 0, 0);
  private static final LocalDateTime SOME_OPEN_TIME = LocalDateTime.of(2018, 9, 22, 14, 30, 0);
  private static final LocalDateTime SOME_CLOSED_TIME = LocalDateTime.of(2018, 9, 22, 3, 0, 0);

  @Mock
  Market market;

  private CloseMarketState state;

  @Before
  public void setupCloseMarketState() {
    state = new CloseMarketState();

    given(market.getOpeningTime()).willReturn(SOME_OPENING_TIME);
    given(market.getClosingTime()).willReturn(SOME_CLOSING_TIME);
  }

  @Test
  public void whenTimeDoesNotOpenTheMarket_thenDoNothing() {
    state.update(market, SOME_CLOSED_TIME);

    verify(market, never()).updateAllStockValues();
    verify(market, never()).setState(any());
  }

  @Test
  public void whenTimeOpensTheMarket_thenUpdateAllStockAndChangeStateToOpen() {
    state.update(market, SOME_OPEN_TIME);

    verify(market).updateAllStockValues();
    verify(market).setState(any(OpenMarketState.class));
  }
}

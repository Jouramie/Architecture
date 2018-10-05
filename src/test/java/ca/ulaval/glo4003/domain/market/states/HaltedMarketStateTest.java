package ca.ulaval.glo4003.domain.market.states;

import static org.mockito.BDDMockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;

import ca.ulaval.glo4003.domain.market.Market;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HaltedMarketStateTest {
  private static final LocalDateTime SOME_TIME = LocalDateTime.of(2018, 9, 22, 14, 30, 0);
  @Mock
  Market market;

  private HaltedMarketState state;

  @Before
  public void setupHaltMarketState() {
    state = new HaltedMarketState();
  }

  @Test
  public void whenUpdate_thenDoNothing() {
    state.update(market, SOME_TIME);

    verify(market, never()).updateAllStockValues();
    verify(market, never()).setState(any());
  }
}
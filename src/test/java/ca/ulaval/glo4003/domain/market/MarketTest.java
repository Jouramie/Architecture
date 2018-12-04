package ca.ulaval.glo4003.domain.market;

import static org.mockito.BDDMockito.verify;

import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarketTest {
  private static final LocalDateTime SOME_TIME = LocalDateTime.of(2018, 9, 22, 15, 0, 0);
  @Mock
  private StockValueRetriever stockValueRetriever;
  private Market market;
  @Mock
  private MarketState state;

  @Before
  public void setUp() {
    market = new MarketBuilder().withState(state).build();
  }

  @Test
  public void whenUpdate_thenUpdateCurrentState() {
    market.update(SOME_TIME, stockValueRetriever);

    verify(state).update(market, SOME_TIME, stockValueRetriever);
  }
}

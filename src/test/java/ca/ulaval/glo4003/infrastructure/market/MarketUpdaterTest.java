package ca.ulaval.glo4003.infrastructure.market;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarketUpdaterTest {
  private static final LocalDateTime SOME_TIME = LocalDateTime.now();
  @Mock
  private Clock someClock;
  @Mock
  private MarketRepository someMarketRepository;
  @Mock
  private Market someMarket;
  @Mock
  private Market someOtherMarket;

  private MarketUpdater marketUpdater;

  @Before
  public void setupMarketUpdater() {
    given(someMarketRepository.getAll()).willReturn(Arrays.asList(someMarket, someOtherMarket));

    marketUpdater = new MarketUpdater(someClock, someMarketRepository);
  }

  @Test
  public void whenConstructorIsCalled_thenMarketUpdaterRegisterItselfToTheClock() {
    MarketUpdater newMarketUpdater = new MarketUpdater(someClock, someMarketRepository);

    verify(someClock).register(newMarketUpdater);
  }

  @Test
  public void whenTick_thenCallUpdateOnAllMarkets() {
    marketUpdater.onTick(SOME_TIME);

    verify(someMarket).update(SOME_TIME);
    verify(someOtherMarket).update(SOME_TIME);
  }
}

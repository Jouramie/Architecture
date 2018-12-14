package ca.ulaval.glo4003.infrastructure.market;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.MarketUpdater;
import ca.ulaval.glo4003.domain.market.state.Market;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
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
  private MarketRepository someMarketRepository;
  @Mock
  private Market someMarket;
  @Mock
  private Market someOtherMarket;
  @Mock
  private StockValueRetriever stockValueRetriever;

  private MarketUpdater marketUpdater;

  @Before
  public void setupMarketsUpdater() {
    given(someMarketRepository.findAll()).willReturn(Arrays.asList(someMarket, someOtherMarket));

    marketUpdater = new MarketUpdater(someMarketRepository, stockValueRetriever);
  }

  @Test
  public void whenTick_thenCallUpdateOnAllMarkets() {
    marketUpdater.onTick(SOME_TIME);

    verify(someMarket).update(SOME_TIME, stockValueRetriever);
    verify(someOtherMarket).update(SOME_TIME, stockValueRetriever);
  }
}

package ca.ulaval.glo4003.service.market;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.TestingMarketBuilder;
import ca.ulaval.glo4003.domain.market.states.Market;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarketServiceTest {

  public static final Market A_MARKET = new TestingMarketBuilder().build();
  public static final MarketId MARKET_ID = A_MARKET.getId();
  @Mock
  private MarketRepository marketRepositoryMock;

  private MarketService service;


  @Before
  public void setUp() throws MarketNotFoundException {
    service = new MarketService(marketRepositoryMock, new MarketStatusAssembler());
    when(marketRepositoryMock.findById(MARKET_ID)).thenReturn(A_MARKET);
  }

  @Test
  public void whenGettingMarketStatus_thenFindsMarketAndMapsItsAttributes() throws MarketDoesNotExistException, MarketNotFoundException {
    MarketStatusDto marketStatus = service.getMarketStatus(MARKET_ID);

    verify(marketRepositoryMock).findById(MARKET_ID);
    assertThat(marketStatus.isHalted).isEqualTo(A_MARKET.isHalted());
    assertThat(marketStatus.haltMessage).isEqualTo(A_MARKET.getHaltMessage());
    assertThat(marketStatus.marketId).isEqualTo(A_MARKET.getId());
  }
}

package ca.ulaval.glo4003.service.market;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.TestingMarketBuilder;
import ca.ulaval.glo4003.domain.market.states.Market;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarketServiceTest {
  public static final String SOME_MESSAGE = "message";
  public static final MarketId SOME_MARKET_ID = new MarketId("market");

  public static final Market SOME_MARKET = new TestingMarketBuilder().build();
  @Mock
  private MarketRepository marketRepositoryMock;

  private MarketService service;


  @Before
  public void setUp() throws MarketNotFoundException {
    service = new MarketService(marketRepositoryMock, new MarketStatusAssembler());
    when(marketRepositoryMock.findById(SOME_MARKET_ID)).thenReturn(SOME_MARKET);
  }

  @Test
  public void whenGettingMarketStatus_thenFindsMarket() throws MarketDoesNotExistException, MarketNotFoundException {
    MarketStatusDto marketStatus = service.getMarketStatus(SOME_MARKET_ID);

    verify(marketRepositoryMock).findById(SOME_MARKET_ID);
    assertThat(marketStatus.isHalted).isEqualTo(SOME_MARKET.isHalted());
    assertThat(marketStatus.haltMessage).isEqualTo(SOME_MARKET.getHaltMessage());
    assertThat(marketStatus.marketId).isEqualTo(SOME_MARKET.getId());
  }

  @Test
  public void whenGettingMarketStatus_thenMapsItsAttributes() throws MarketDoesNotExistException, MarketNotFoundException {
    MarketStatusDto marketStatus = service.getMarketStatus(SOME_MARKET_ID);

    assertThat(marketStatus.isHalted).isEqualTo(SOME_MARKET.isHalted());
    assertThat(marketStatus.haltMessage).isEqualTo(SOME_MARKET.getHaltMessage());
    assertThat(marketStatus.marketId).isEqualTo(SOME_MARKET.getId());
  }

  @Test
  public void givenInexistentMarket_whenHaltingMarket_thenThrowException() throws MarketNotFoundException {
    given(marketRepositoryMock.findById(SOME_MARKET_ID)).willThrow(new MarketNotFoundException(""));

    ThrowableAssert.ThrowingCallable haltCallable = () -> service.haltMarket(SOME_MARKET_ID, SOME_MESSAGE);
    assertThatThrownBy(haltCallable).isInstanceOf(MarketDoesNotExistException.class);
  }

  @Test
  public void whenHaltingMarket_thenUpdateMarketState() throws MarketNotFoundException, MarketDoesNotExistException {
    given(marketRepositoryMock.findById(SOME_MARKET_ID)).willReturn(SOME_MARKET);

    service.haltMarket(SOME_MARKET_ID, SOME_MESSAGE);

    assertThat(SOME_MARKET.isHalted()).isTrue();
    assertThat(SOME_MARKET.getHaltMessage()).isEqualTo(SOME_MESSAGE);
  }
}

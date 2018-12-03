package ca.ulaval.glo4003.service.market;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.domain.market.MarketBuilder;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
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

  @Mock
  private MarketRepository marketRepositoryMock;

  private MarketService service;

  private Market market;

  @Before
  public void setUp() throws MarketNotFoundException {
    service = new MarketService(marketRepositoryMock, new MarketStatusAssembler());
    market = new MarketBuilder().build();
    given(marketRepositoryMock.findById(SOME_MARKET_ID)).willReturn(market);
  }

  @Test
  public void whenGettingMarketStatus_thenFindsMarket() throws MarketDoesNotExistException, MarketNotFoundException {
    service.getMarketStatus(SOME_MARKET_ID);

    verify(marketRepositoryMock).findById(SOME_MARKET_ID);
  }

  @Test
  public void whenGettingMarketStatus_thenMapsItsAttributes() throws MarketDoesNotExistException {
    MarketStatusDto marketStatus = service.getMarketStatus(SOME_MARKET_ID);

    assertThat(marketStatus.isHalted).isEqualTo(market.isHalted());
    assertThat(marketStatus.haltMessage).isEqualTo(market.getHaltMessage());
    assertThat(marketStatus.marketId).isEqualTo(market.getId());
  }

  @Test
  public void givenInexistentMarket_whenHaltingMarket_thenThrowException() throws MarketNotFoundException {
    given(marketRepositoryMock.findById(SOME_MARKET_ID)).willThrow(new MarketNotFoundException(""));

    ThrowableAssert.ThrowingCallable haltCallable = () -> service.haltMarket(SOME_MARKET_ID, SOME_MESSAGE);
    assertThatThrownBy(haltCallable).isInstanceOf(MarketDoesNotExistException.class);
  }

  @Test
  public void whenHaltingMarket_thenUpdateMarketState() throws MarketDoesNotExistException {
    service.haltMarket(SOME_MARKET_ID, SOME_MESSAGE);

    assertThat(market.isHalted()).isTrue();
    assertThat(market.getHaltMessage()).isEqualTo(SOME_MESSAGE);
  }

  @Test
  public void whenResuming_thenUpdateMarketState() throws MarketDoesNotExistException {
    market.halt("");

    service.resumeMarket(SOME_MARKET_ID);

    assertThat(market.isHalted()).isFalse();
  }

  @Test
  public void givenInexistentMarket_whenResumingMarket_thenThrowException() throws MarketNotFoundException {
    given(marketRepositoryMock.findById(SOME_MARKET_ID)).willThrow(new MarketNotFoundException(""));

    ThrowableAssert.ThrowingCallable resumeCallable = () -> service.resumeMarket(SOME_MARKET_ID);

    assertThatThrownBy(resumeCallable).isInstanceOf(MarketDoesNotExistException.class);
  }
}

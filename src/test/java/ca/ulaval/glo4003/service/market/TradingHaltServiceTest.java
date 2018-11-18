package ca.ulaval.glo4003.service.market;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
public class TradingHaltServiceTest {

  public static final String MESSAGE = "message";
  public static final MarketId MARKET_ID = new MarketId("market");
  private TradingHaltService service;

  private Market market;

  @Mock
  private MarketRepository marketRepositoryMock;

  @Before
  public void setUp() {
    service = new TradingHaltService(marketRepositoryMock);
    market = new TestingMarketBuilder().build();
  }

  @Test
  public void givenInexistentMarket_whenHaltingMarket_thenThrowException() throws MarketNotFoundException {
    when(marketRepositoryMock.findById(MARKET_ID)).thenThrow(new MarketNotFoundException(""));

    assertThatThrownBy(() -> service.haltMarket(MARKET_ID, MESSAGE)).isInstanceOf(MarketDoesNotExistException.class);
  }

  @Test
  public void whenHaltingMarket_thenUpdateMarketState() throws MarketNotFoundException, MarketDoesNotExistException {
    when(marketRepositoryMock.findById(MARKET_ID)).thenReturn(market);

    service.haltMarket(MARKET_ID, MESSAGE);

    assertThat(market.isHalted()).isTrue();
    assertThat(market.getHaltMessage()).isEqualTo(MESSAGE);
  }
}

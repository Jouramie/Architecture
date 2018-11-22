package ca.ulaval.glo4003.ws.api.market.halt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.TestingMarketBuilder;
import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.service.market.MarketDoesNotExistException;
import ca.ulaval.glo4003.service.market.MarketService;
import ca.ulaval.glo4003.service.market.MarketStatusDto;
import ca.ulaval.glo4003.ws.api.market.ApiMarketStatusAssembler;
import ca.ulaval.glo4003.ws.api.market.dto.MarketStatusResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarketHaltResourceImplTest {

  public static final String MESSAGE = "message";
  public static final Market MARKET = new TestingMarketBuilder().halted(MESSAGE).build();
  public static final MarketId MARKET_ID = MARKET.getId();

  private MarketHaltResourceImpl marketHaltResource;

  @Mock
  private MarketService marketServiceMock;

  @Before
  public void setUp() throws MarketDoesNotExistException {
    marketHaltResource = new MarketHaltResourceImpl(marketServiceMock, new ApiMarketStatusAssembler());
    when(marketServiceMock.getMarketStatus(MARKET_ID)).thenReturn(new MarketStatusDto(MARKET_ID, true, MESSAGE));
  }

  @Test
  public void whenHaltingMarket_thenReturnMarketStatus() throws MarketDoesNotExistException {
    MarketStatusResponseDto marketStatusResponseDto = marketHaltResource.haltMarket("market", MESSAGE);

    assertThat(marketStatusResponseDto.market).isEqualTo(MARKET_ID.getValue());
    assertThat(marketStatusResponseDto.status).isEqualTo(String.format("HALTED: %s", MESSAGE));
  }
}

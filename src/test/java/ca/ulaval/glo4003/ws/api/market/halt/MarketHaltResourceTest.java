package ca.ulaval.glo4003.ws.api.market.halt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.domain.market.MarketBuilder;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.service.market.MarketDoesNotExistException;
import ca.ulaval.glo4003.service.market.MarketService;
import ca.ulaval.glo4003.service.market.MarketStatusDto;
import ca.ulaval.glo4003.ws.api.market.MarketHaltResourceImpl;
import ca.ulaval.glo4003.ws.api.market.assemblers.ApiMarketStatusAssembler;
import ca.ulaval.glo4003.ws.api.market.dto.MarketStatusResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarketHaltResourceTest {

  public static final String SOME_MESSAGE = "message";
  public static final Market SOME_MARKET = new MarketBuilder().halted(SOME_MESSAGE).build();
  public static final MarketId SOME_MARKET_ID = SOME_MARKET.getId();

  private MarketHaltResourceImpl marketHaltResource;

  @Mock
  private MarketService marketServiceMock;

  @Before
  public void setUp() throws MarketDoesNotExistException {
    marketHaltResource = new MarketHaltResourceImpl(marketServiceMock, new ApiMarketStatusAssembler());
    when(marketServiceMock.getMarketStatus(SOME_MARKET_ID)).thenReturn(new MarketStatusDto(SOME_MARKET_ID, true, SOME_MESSAGE));
  }

  @Test
  public void whenHaltingMarket_thenReturnMarketStatus() throws MarketDoesNotExistException {
    MarketStatusResponseDto marketStatusResponseDto = marketHaltResource.haltMarket(SOME_MARKET_ID.getValue(), SOME_MESSAGE);

    assertThat(marketStatusResponseDto.market).isEqualTo(SOME_MARKET_ID.getValue());
    assertThat(marketStatusResponseDto.status).isEqualTo("HALTED");
    assertThat(marketStatusResponseDto.haltMessage).isEqualTo(SOME_MESSAGE);
  }

  @Test
  public void whenHaltingMarket_thenServiceHaltsMarket() throws MarketDoesNotExistException {
    marketHaltResource.haltMarket(SOME_MARKET_ID.getValue(), SOME_MESSAGE);

    verify(marketServiceMock).haltMarket(SOME_MARKET_ID, SOME_MESSAGE);
  }

  @Test
  public void whenResumingMarket_thenServiceResumesMarket() throws MarketDoesNotExistException {
    marketHaltResource.resumeMarket(SOME_MARKET_ID.getValue());

    verify(marketServiceMock).resumeMarket(SOME_MARKET_ID);
  }
}

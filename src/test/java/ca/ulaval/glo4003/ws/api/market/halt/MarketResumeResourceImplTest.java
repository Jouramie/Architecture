package ca.ulaval.glo4003.ws.api.market.halt;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.service.market.MarketDoesNotExistException;
import ca.ulaval.glo4003.service.market.MarketService;
import ca.ulaval.glo4003.service.market.MarketStatusDto;
import ca.ulaval.glo4003.service.market.TradingHaltService;
import ca.ulaval.glo4003.ws.api.market.ApiMarketStatusAssembler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarketResumeResourceImplTest {

  public static final MarketId MARKET_ID = new MarketId("London");
  @Mock
  private MarketService marketService;
  @Mock
  private TradingHaltService tradingHaltService;

  private MarketResumeResource resource;

  @Before
  public void setUp() throws MarketDoesNotExistException {
    resource = new MarketResumeResourceImpl(marketService, tradingHaltService, new ApiMarketStatusAssembler());
    when(marketService.getMarketStatus(MARKET_ID)).thenReturn(new MarketStatusDto(MARKET_ID, false, ""));
  }

  @Test
  public void whenResumingMarket_thenServiceResumesMarket() throws MarketDoesNotExistException {
    resource.resumeMarket("London");

    verify(tradingHaltService).resumeMarket(new MarketId("London"));
  }
}

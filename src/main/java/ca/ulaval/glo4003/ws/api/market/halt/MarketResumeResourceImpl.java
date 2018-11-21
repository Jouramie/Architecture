package ca.ulaval.glo4003.ws.api.market.halt;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.service.market.MarketDoesNotExistException;
import ca.ulaval.glo4003.service.market.MarketService;
import ca.ulaval.glo4003.service.market.MarketStatusDto;
import ca.ulaval.glo4003.service.market.TradingHaltService;
import ca.ulaval.glo4003.ws.api.market.ApiMarketStatusAssembler;
import ca.ulaval.glo4003.ws.api.market.dto.MarketStatusResponseDto;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class MarketResumeResourceImpl implements MarketResumeResource {

  private final MarketService marketService;
  private final TradingHaltService tradingHaltService;
  private final ApiMarketStatusAssembler apiMarketStatusAssembler;

  @Inject
  public MarketResumeResourceImpl(MarketService marketService, TradingHaltService tradingHaltService, ApiMarketStatusAssembler apiMarketStatusAssembler) {
    this.marketService = marketService;
    this.tradingHaltService = tradingHaltService;
    this.apiMarketStatusAssembler = apiMarketStatusAssembler;
  }

  @Override
  public MarketStatusResponseDto haltMarket(String market) throws MarketDoesNotExistException {
    MarketId marketId = new MarketId(market);
    tradingHaltService.resumeMarket(marketId);
    MarketStatusDto marketStatus = marketService.getMarketStatus(marketId);
    return apiMarketStatusAssembler.toDto(marketStatus);
  }
}

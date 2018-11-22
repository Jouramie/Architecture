package ca.ulaval.glo4003.ws.api.market.halt;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.service.market.MarketDoesNotExistException;
import ca.ulaval.glo4003.service.market.MarketService;
import ca.ulaval.glo4003.service.market.MarketStatusDto;
import ca.ulaval.glo4003.ws.api.market.ApiMarketStatusAssembler;
import ca.ulaval.glo4003.ws.api.market.dto.MarketStatusResponseDto;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class MarketHaltResourceImpl implements MarketHaltResource {

  private final MarketService marketService;
  private final ApiMarketStatusAssembler apiMarketStatusAssembler;

  @Inject
  public MarketHaltResourceImpl(MarketService marketService, ApiMarketStatusAssembler apiMarketStatusAssembler) {
    this.marketService = marketService;
    this.apiMarketStatusAssembler = apiMarketStatusAssembler;
  }

  @Override
  public MarketStatusResponseDto haltMarket(String market, String message) throws MarketDoesNotExistException {
    MarketId marketId = new MarketId(market);
    marketService.haltMarket(marketId, message);
    MarketStatusDto marketStatus = marketService.getMarketStatus(marketId);
    return apiMarketStatusAssembler.toDto(marketStatus);
  }

  @Override
  public MarketStatusResponseDto resumeMarket(String market) throws MarketDoesNotExistException {
    MarketId marketId = new MarketId(market);
    marketService.resumeMarket(marketId);
    MarketStatusDto marketStatus = marketService.getMarketStatus(marketId);
    return apiMarketStatusAssembler.toDto(marketStatus);
  }
}

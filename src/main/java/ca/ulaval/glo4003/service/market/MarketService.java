package ca.ulaval.glo4003.service.market;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.states.Market;
import javax.inject.Inject;

@Component
public class MarketService {

  private final MarketRepository marketRepository;
  private final MarketStatusAssembler marketStatusAssembler;

  @Inject
  public MarketService(MarketRepository marketRepository, MarketStatusAssembler marketStatusAssembler) {
    this.marketRepository = marketRepository;
    this.marketStatusAssembler = marketStatusAssembler;
  }

  public MarketStatusDto getMarketStatus(MarketId marketId) throws MarketDoesNotExistException {
    try {
      Market market = marketRepository.findById(marketId);
      return marketStatusAssembler.toDto(market);
    } catch (MarketNotFoundException e) {
      throw new MarketDoesNotExistException();
    }
  }
}

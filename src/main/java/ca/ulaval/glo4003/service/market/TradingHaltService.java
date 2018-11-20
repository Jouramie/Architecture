package ca.ulaval.glo4003.service.market;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.states.Market;
import javax.inject.Inject;

@Component
public class TradingHaltService {

  private final MarketRepository marketRepository;

  @Inject
  public TradingHaltService(MarketRepository marketRepository) {
    this.marketRepository = marketRepository;
  }

  public void haltMarket(MarketId marketId, String haltMessage) throws MarketDoesNotExistException {
    try {
      Market market = marketRepository.findById(marketId);
      market.halt(haltMessage);
    } catch (MarketNotFoundException e) {
      throw new MarketDoesNotExistException();
    }
  }

  public void resumeMarket(MarketId marketId) throws MarketDoesNotExistException {
    try {
      Market market = marketRepository.findById(marketId);
      market.resume();
    } catch (MarketNotFoundException e) {
      throw new MarketDoesNotExistException();
    }
  }
}

package ca.ulaval.glo4003.domain.market;

import ca.ulaval.glo4003.domain.market.states.Market;
import java.util.List;

public interface MarketRepository {
  List<Market> findAll();

  Market findById(MarketId id) throws MarketNotFoundException;

  void add(Market market);

  Market findMarketForStock(String stockTitle) throws MarketNotFoundForStockException;
}

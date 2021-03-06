package ca.ulaval.glo4003.domain.market;

import ca.ulaval.glo4003.domain.market.exception.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.state.Market;
import java.util.List;

public interface MarketRepository {
  List<Market> findAll();

  Market findById(MarketId id) throws MarketNotFoundException;

  void add(Market market);

  Market findByStock(String stockTitle) throws MarketNotFoundException;
}

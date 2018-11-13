package ca.ulaval.glo4003.infrastructure.persistence;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.states.Market;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryMarketRepository implements MarketRepository {
  private final Map<MarketId, Market> markets = new HashMap<>();

  @Override
  public List<Market> findAll() {
    return markets.values().stream().collect(Collectors.toList());
  }

  @Override
  public Market findById(MarketId id) throws MarketNotFoundException {
    Market result = markets.get(id);
    if (result == null) {
      throw new MarketNotFoundException("Cannot find market with id " + id.getValue());
    }

    return result;
  }

  @Override
  public void add(Market stock) {
    markets.put(stock.getId(), stock);
  }
}

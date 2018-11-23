package ca.ulaval.glo4003.infrastructure.persistence;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.market.states.Market;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryMarketRepository implements MarketRepository {
  private final Map<MarketId, Market> markets = new HashMap<>();

  @Override
  public List<Market> findAll() {
    return new ArrayList<>(markets.values());
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

  @Override
  public Market findMarketForStock(String stockTitle) throws MarketNotFoundException {
    return markets.values().stream()
        .filter(market -> market.containsStock(stockTitle))
        .findFirst()
        .orElseThrow(() -> new MarketNotFoundException("Cannot find market for stock " + stockTitle));
  }
}

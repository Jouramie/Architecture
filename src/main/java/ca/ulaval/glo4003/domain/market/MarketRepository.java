package ca.ulaval.glo4003.domain.market;

import java.util.List;

public interface MarketRepository {
  List<Market> getAll();

  Market getById(MarketId id) throws MarketNotFoundException;

  void add(Market market);
}

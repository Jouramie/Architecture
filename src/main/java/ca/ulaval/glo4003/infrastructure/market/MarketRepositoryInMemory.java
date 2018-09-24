package ca.ulaval.glo4003.infrastructure.market;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketNotFoundException;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarketRepositoryInMemory implements MarketRepository {
    private Map<MarketId, Market> markets = new HashMap<>();

    @Override
    public List<Market> getAll() {
        return markets.values().stream().collect(Collectors.toList());
    }

    @Override
    public Market getById(MarketId id) {
        Market result = markets.get(id);
        if(result == null) {
            throw new MarketNotFoundException("Cannot find market with id " + id.getValue());
        }

        return result;
    }

    public void add(Market stock) {
        markets.put(stock.getId(), stock);
    }
}

package ca.ulaval.glo4003.infrastructure.persistence;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryStockRepository implements StockRepository {
  private final Map<String, Stock> stocks = new HashMap<>();

  @Override
  public List<Stock> getAll() {
    return new ArrayList<>(stocks.values());
  }

  @Override
  public Stock getByTitle(String title) throws StockNotFoundException {
    Stock result = stocks.get(title);
    if (result == null) {
      throw new StockNotFoundException(title);
    }
    return result;
  }

  @Override
  public Stock getByName(String name) throws StockNotFoundException {
    return stocks.values().stream().filter((stock) -> stock.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new StockNotFoundException("Cannot find stock with name " + name));
  }

  @Override
  public List<Stock> getByMarket(MarketId marketId) {
    return stocks.values().stream().filter((stock) -> stock.getMarketId().equals(marketId))
        .collect(Collectors.toList());
  }

  @Override
  public void add(Stock stock) {
    stocks.put(stock.getTitle(), stock);
  }

  @Override
  public boolean doesStockExist(String title) {
    return stocks.containsKey(title);
  }
}

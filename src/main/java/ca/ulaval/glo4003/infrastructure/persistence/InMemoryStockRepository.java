package ca.ulaval.glo4003.infrastructure.persistence;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQueryByNameAndCategory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryStockRepository implements StockRepository {
  private final Map<String, Stock> stocks = new HashMap<>();

  @Override
  public List<Stock> findAll() {
    return new ArrayList<>(stocks.values());
  }

  @Override
  public Stock findByTitle(String title) throws StockNotFoundException {
    Stock result = stocks.get(title);
    if (result == null) {
      throw new StockNotFoundException(title);
    }
    return result;
  }

  @Override
  public List<Stock> findByTitles(List<String> titles) throws StockNotFoundException {
    List<Stock> list = new ArrayList<>();
    for (String title : titles) {
      Stock byTitle = findByTitle(title);
      list.add(byTitle);
    }
    return list;
  }

  @Override
  public List<Stock> findByMarket(MarketId marketId) {
    return stocks.values().stream().filter((stock) -> stock.getMarketId().equals(marketId))
        .collect(toList());
  }

  @Override
  public void add(Stock stock) {
    stocks.put(stock.getTitle(), stock);
  }

  @Override
  public boolean exists(String title) {
    return stocks.containsKey(title);
  }

  @Override
  public List<String> findAllCategories() {
    return stocks.values().stream().map(Stock::getCategory).distinct().collect(toList());
  }

  @Override
  public List<Stock> queryStocks(StockQueryByNameAndCategory stockQuery) {
    return stocks.values().stream().filter(stockQuery).collect(toList());
  }
}

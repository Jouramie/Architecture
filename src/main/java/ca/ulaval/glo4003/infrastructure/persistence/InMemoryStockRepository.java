package ca.ulaval.glo4003.infrastructure.persistence;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryStockRepository implements StockRepository {
  private final Map<String, Stock> stocks = new HashMap<>();

  @Override
  public void add(Stock stock) {
    stocks.put(stock.getTitle(), stock);
  }

  @Override
  public boolean exists(String title) {
    return stocks.containsKey(title);
  }

  @Override
  public List<Stock> find(StockQuery stockQuery) {
    return stockQuery.getMatchingStocks(new ArrayList<>(stocks.values()));
  }

  @Override
  public List<String> findAllCategories() {
    return stocks.values().stream().map(Stock::getCategory).distinct().collect(toList());
  }
}

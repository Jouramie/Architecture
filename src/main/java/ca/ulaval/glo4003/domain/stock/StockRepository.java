package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import java.util.List;
import java.util.function.Predicate;

public interface StockRepository {
  List<Stock> findAll();

  Stock findByTitle(String title) throws StockNotFoundException;

  List<Stock> findByTitles(List<String> titles) throws StockNotFoundException;

  List<Stock> findByMarket(MarketId marketId);

  void add(Stock stock);

  boolean exists(String title);

  List<String> findAllCategories();

  List<Stock> queryStocks(Predicate<Stock> stockPredicate);
}

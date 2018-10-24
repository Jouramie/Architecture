package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import java.util.List;

public interface StockRepository {
  List<Stock> findAll();

  Stock findByTitle(String title) throws StockNotFoundException;

  List<Stock> findByMarket(MarketId marketId);

  void add(Stock stock);

  boolean doesStockExist(String title);

  List<String> findAllCategories();

  List<Stock> queryStocks(String name, String category);
}

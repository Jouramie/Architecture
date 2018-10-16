package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import java.util.List;

public interface StockRepository {
  List<Stock> getAll();

  Stock getByTitle(String title) throws StockNotFoundException;

  Stock getByName(String name) throws StockNotFoundException;

  List<Stock> getByMarket(MarketId marketId);

  void add(Stock stock);

  boolean doesStockExist(String title);

  List<String> getCategories();

  List<Stock> queryStocks(String name, String category);
}

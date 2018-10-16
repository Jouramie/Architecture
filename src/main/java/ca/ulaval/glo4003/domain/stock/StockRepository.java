package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.market.MarketId;
import com.sun.istack.internal.Nullable;
import java.util.List;

public interface StockRepository {
  List<Stock> getAll();

  Stock getByTitle(String title) throws StockNotFoundException;

  List<Stock> getByMarket(MarketId marketId);

  void add(Stock stock);

  boolean doesStockExist(String title);

  List<String> getCategories();

  List<Stock> queryStocks(@Nullable String name, @Nullable String category);
}

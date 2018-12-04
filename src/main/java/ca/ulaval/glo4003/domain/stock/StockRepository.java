package ca.ulaval.glo4003.domain.stock;

import ca.ulaval.glo4003.domain.stock.query.StockQuery;
import java.util.List;

public interface StockRepository {
  void add(Stock stock);

  boolean exists(String title);

  List<Stock> find(StockQuery stockQuery);

  List<String> findAllCategories();
}

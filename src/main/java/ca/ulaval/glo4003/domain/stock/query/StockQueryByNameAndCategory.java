package ca.ulaval.glo4003.domain.stock.query;

import ca.ulaval.glo4003.domain.stock.Stock;
import java.util.Objects;
import java.util.function.Predicate;

public class StockQueryByNameAndCategory implements Predicate<Stock> {
  private final String name;
  private final String category;

  public StockQueryByNameAndCategory(String name, String category) {
    this.name = name;
    this.category = category;
  }

  @Override
  public boolean test(Stock stock) {
    return doesStockNameRespectQuery(stock.getName()) && doesStockCategoryRespectQuery(stock.getCategory());
  }

  private boolean doesStockNameRespectQuery(String stockName) {
    if (name == null) {
      return true;
    }

    return stockName.equals(name);
  }

  private boolean doesStockCategoryRespectQuery(String stockCategory) {
    if (category == null) {
      return true;
    }

    return stockCategory.equals(category);
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockQueryByNameAndCategory that = (StockQueryByNameAndCategory) o;
    return name.equals(that.name) && category.equals(that.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, category);
  }
}

package ca.ulaval.glo4003.domain.stock.query;

import ca.ulaval.glo4003.domain.stock.Stock;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class StockQuery implements Predicate<Stock> {
  private final List<String> names;
  private final List<String> categories;

  public StockQuery(List<String> names, List<String> categories) {
    this.names = names;
    this.categories = categories;
  }

  @Override
  public boolean test(Stock stock) {
    List<String> names = getNamesForTestOnStock(stock);
    List<String> categories = getCategoriesForTestOnStock(stock);

    return names.contains(stock.getName()) && categories.contains(stock.getCategory());
  }

  private List<String> getNamesForTestOnStock(Stock stock) {
    if (names != null) {
      return names;
    } else {
      return Arrays.asList(stock.getName());
    }
  }

  private List<String> getCategoriesForTestOnStock(Stock stock) {
    if (categories != null) {
      return categories;
    } else {
      return Arrays.asList(stock.getCategory());
    }
  }

  public List<String> getNames() {
    return names;
  }

  public List<String> getCategories() {
    return categories;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockQuery that = (StockQuery) o;
    return Objects.equals(names, that.names) && Objects.equals(categories, that.categories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(names, categories);
  }
}

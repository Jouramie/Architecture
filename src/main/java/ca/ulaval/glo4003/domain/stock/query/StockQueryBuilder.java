package ca.ulaval.glo4003.domain.stock.query;

import java.util.ArrayList;
import java.util.List;

public class StockQueryBuilder {
  private List<String> names = null;
  private List<String> categories = null;

  public StockQueryBuilder withNames(List<String> names) {
    this.names = names;
    return this;
  }

  public StockQueryBuilder withName(String name) {
    if (name != null) {
      if (names == null) {
        names = new ArrayList<>();
      }
      names.add(name);
    }
    return this;
  }

  public StockQueryBuilder withCategories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public StockQueryBuilder withCategory(String category) {
    if (category != null) {
      if (categories == null) {
        categories = new ArrayList<>();
      }
      categories.add(category);
    }
    return this;
  }

  public StockQuery build() {
    return new StockQuery(names, categories);
  }
}

package ca.ulaval.glo4003.domain.stock.query;

public class StockQueryByNameAndCategoryBuilder {
  private String name = null;
  private String category = null;

  public StockQueryByNameAndCategoryBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public StockQueryByNameAndCategoryBuilder withCategory(String category) {
    this.category = category;
    return this;
  }

  public StockQueryByNameAndCategory build() {
    return new StockQueryByNameAndCategory(name, category);
  }
}

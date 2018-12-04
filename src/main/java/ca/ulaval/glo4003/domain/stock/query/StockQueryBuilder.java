package ca.ulaval.glo4003.domain.stock.query;

import ca.ulaval.glo4003.domain.market.MarketId;
import java.util.ArrayList;
import java.util.List;

public class StockQueryBuilder {
  private List<String> titles = null;
  private List<String> names = null;
  private List<MarketId> marketIds = null;
  private List<String> categories = null;

  public StockQueryBuilder withTitles(List<String> titles) {
    this.titles = titles;
    return this;
  }

  public StockQueryBuilder withTitle(String title) {
    if (titles == null) {
      titles = new ArrayList<>();
    }
    titles.add(title);
    return this;
  }

  public StockQueryBuilder withNames(List<String> names) {
    this.names = names;
    return this;
  }

  public StockQueryBuilder withName(String name) {
    if (names == null) {
      names = new ArrayList<>();
    }
    names.add(name);
    return this;
  }

  public StockQueryBuilder withMarketIds(List<MarketId> marketIds) {
    this.marketIds = marketIds;
    return this;
  }

  public StockQueryBuilder withMarketId(MarketId marketId) {
    if (marketIds == null) {
      marketIds = new ArrayList<>();
    }
    marketIds.add(marketId);
    return this;
  }

  public StockQueryBuilder withCategories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public StockQueryBuilder withCategory(String category) {
    if (categories == null) {
      categories = new ArrayList<>();
    }
    categories.add(category);
    return this;
  }

  public StockQuery build() {
    return new StockQuery(titles, names, marketIds, categories);
  }
}

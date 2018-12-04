package ca.ulaval.glo4003.domain.stock.query;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import java.util.List;

public class StockQuery {
  private final List<String> titles;
  private final List<String> names;
  private final List<MarketId> marketIds;
  private final List<String> categories;

  public StockQuery(List<String> titles, List<String> names, List<MarketId> marketIds, List<String> categories) {
    this.titles = titles;
    this.names = names;
    this.marketIds = marketIds;
    this.categories = categories;
  }

  public List<Stock> getMatchingStocks(List<Stock> stocks) {
    List<String> titles = getTitlesForQueryOnStocks(stocks);
    List<String> names = getNamesForQueryOnStocks(stocks);
    List<MarketId> marketIds = getMarketIdsForQueryOnStocks(stocks);
    List<String> categories = getCategoriesForQueryOnStocks(stocks);

    return stocks.stream().filter(stock ->
        titles.contains(stock.getTitle())
            && names.contains(stock.getName())
            && marketIds.contains(stock.getMarketId())
            && categories.contains(stock.getCategory())
    ).collect(toList());
  }

  private List<String> getTitlesForQueryOnStocks(List<Stock> stocks) {
    if (titles != null) {
      return titles;
    } else {
      return stocks.stream().map(Stock::getTitle).collect(toList());
    }
  }

  private List<String> getNamesForQueryOnStocks(List<Stock> stocks) {
    if (names != null) {
      return names;
    } else {
      return stocks.stream().map(Stock::getName).collect(toList());
    }
  }

  private List<MarketId> getMarketIdsForQueryOnStocks(List<Stock> stocks) {
    if (marketIds != null) {
      return marketIds;
    } else {
      return stocks.stream().map(Stock::getMarketId).collect(toList());
    }
  }

  private List<String> getCategoriesForQueryOnStocks(List<Stock> stocks) {
    if (categories != null) {
      return categories;
    } else {
      return stocks.stream().map(Stock::getCategory).collect(toList());
    }
  }

  public List<String> getTitles() {
    return titles;
  }

  public List<String> getNames() {
    return names;
  }

  public List<MarketId> getMarketIds() {
    return marketIds;
  }

  public List<String> getCategories() {
    return categories;
  }
}

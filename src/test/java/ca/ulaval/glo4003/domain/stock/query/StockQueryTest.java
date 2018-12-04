package ca.ulaval.glo4003.domain.stock.query;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class StockQueryTest {
  private static final String FIRST_STOCK_TITLE = "firstTitle";
  private static final String FIRST_STOCK_NAME = "firstName";
  private static final String SECOND_STOCK_TITLE = "secondTitle";
  private static final String SECOND_STOCK_NAME = "secondName";
  private final static String THIRD_STOCK_TITLE = "thirdTitle";
  private static final String THIRD_STOCK_NAME = "thirdName";
  private static final MarketId FIRST_STOCK_MARKET_ID = new MarketId("id1");
  private static final MarketId SECOND_STOCK_MARKET_ID = new MarketId("id2");
  private static final MarketId THIRD_STOCK_MARKET_ID = new MarketId("id3");
  private static final String FIRST_STOCK_CATEGORY = "firstCategory";
  private static final String SECOND_STOCK_CATEGORY = "secondCategory";
  private static final String THIRD_STOCK_CATEGORY = "thirdCategory";

  private static final Stock firstStock = new TestStockBuilder()
      .withTitle(FIRST_STOCK_TITLE).withName(FIRST_STOCK_NAME)
      .withMarketId(FIRST_STOCK_MARKET_ID)
      .withCategory(FIRST_STOCK_CATEGORY).build();
  private static final Stock secondStock = new TestStockBuilder()
      .withTitle(SECOND_STOCK_TITLE).withName(SECOND_STOCK_NAME)
      .withMarketId(SECOND_STOCK_MARKET_ID)
      .withCategory(SECOND_STOCK_CATEGORY).build();
  private static final Stock thirdStock = new TestStockBuilder()
      .withTitle(THIRD_STOCK_TITLE).withName(THIRD_STOCK_NAME)
      .withMarketId(THIRD_STOCK_MARKET_ID)
      .withCategory(THIRD_STOCK_CATEGORY).build();
  private final List<Stock> ALL_STOCKS = Arrays.asList(firstStock, secondStock, thirdStock);

  @Test
  public void givenQueryWithNullTitleList_whenGettingMatchingStocks_thenReturnAllStocks() {
    StockQuery stockQuery = new StockQuery(null,
        Arrays.asList(FIRST_STOCK_NAME, SECOND_STOCK_NAME, THIRD_STOCK_NAME),
        Arrays.asList(FIRST_STOCK_MARKET_ID, SECOND_STOCK_MARKET_ID, THIRD_STOCK_MARKET_ID),
        Arrays.asList(FIRST_STOCK_CATEGORY, SECOND_STOCK_CATEGORY, THIRD_STOCK_CATEGORY));

    List<Stock> returnedStocks = stockQuery.getMatchingStocks(ALL_STOCKS);

    assertThat(returnedStocks, containsInAnyOrder(ALL_STOCKS.toArray()));
  }

  @Test
  public void givenQueryWithNullNameList_whenGettingMatchingStocks_thenReturnAllStocks() {
    StockQuery stockQuery = new StockQuery(
        Arrays.asList(FIRST_STOCK_TITLE, SECOND_STOCK_TITLE, THIRD_STOCK_TITLE),
        null,
        Arrays.asList(FIRST_STOCK_MARKET_ID, SECOND_STOCK_MARKET_ID, THIRD_STOCK_MARKET_ID),
        Arrays.asList(FIRST_STOCK_CATEGORY, SECOND_STOCK_CATEGORY, THIRD_STOCK_CATEGORY));

    List<Stock> returnedStocks = stockQuery.getMatchingStocks(ALL_STOCKS);

    assertThat(returnedStocks, containsInAnyOrder(ALL_STOCKS.toArray()));
  }

  @Test
  public void givenQueryWithNullMarketIdList_whenGettingMatchingStocks_thenReturnAllStocks() {
    StockQuery stockQuery = new StockQuery(
        Arrays.asList(FIRST_STOCK_TITLE, SECOND_STOCK_TITLE, THIRD_STOCK_TITLE),
        Arrays.asList(FIRST_STOCK_NAME, SECOND_STOCK_NAME, THIRD_STOCK_NAME),
        null,
        Arrays.asList(FIRST_STOCK_CATEGORY, SECOND_STOCK_CATEGORY, THIRD_STOCK_CATEGORY));

    List<Stock> returnedStocks = stockQuery.getMatchingStocks(ALL_STOCKS);

    assertThat(returnedStocks, containsInAnyOrder(ALL_STOCKS.toArray()));
  }

  @Test
  public void givenQueryWithNullCategoryList_whenGettingMatchingStocks_thenReturnAllStocks() {
    StockQuery stockQuery = new StockQuery(
        Arrays.asList(FIRST_STOCK_TITLE, SECOND_STOCK_TITLE, THIRD_STOCK_TITLE),
        Arrays.asList(FIRST_STOCK_NAME, SECOND_STOCK_NAME, THIRD_STOCK_NAME),
        Arrays.asList(FIRST_STOCK_MARKET_ID, SECOND_STOCK_MARKET_ID, THIRD_STOCK_MARKET_ID),
        null);

    List<Stock> returnedStocks = stockQuery.getMatchingStocks(ALL_STOCKS);

    assertThat(returnedStocks, containsInAnyOrder(ALL_STOCKS.toArray()));
  }

  @Test
  public void givenQueryWithMultipleTitles_whenGettingMatchingStocks_thenReturnStocksWithTitleInList() {
    StockQuery stockQuery = new StockQuery(Arrays.asList(THIRD_STOCK_TITLE, SECOND_STOCK_TITLE), null,
        null, null);

    List<Stock> returnedStocks = stockQuery.getMatchingStocks(ALL_STOCKS);

    assertThat(returnedStocks, containsInAnyOrder(secondStock, thirdStock));
  }

  @Test
  public void givenQueryWithMultipleNames_whenGettingMatchingStocks_thenReturnStocksWithNameInList() {
    StockQuery stockQuery = new StockQuery(null, Arrays.asList(THIRD_STOCK_NAME, FIRST_STOCK_NAME),
        null, null);

    List<Stock> returnedStocks = stockQuery.getMatchingStocks(ALL_STOCKS);

    assertThat(returnedStocks, containsInAnyOrder(firstStock, thirdStock));
  }

  @Test
  public void givenQueryWithMultipleMarketIds_whenGettingMatchingStocks_thenReturnStocksWithMarketIdInList() {
    StockQuery stockQuery = new StockQuery(null, null,
        Arrays.asList(SECOND_STOCK_MARKET_ID, FIRST_STOCK_MARKET_ID), null);

    List<Stock> returnedStocks = stockQuery.getMatchingStocks(ALL_STOCKS);

    assertThat(returnedStocks, containsInAnyOrder(firstStock, secondStock));
  }

  @Test
  public void givenQueryWithMultipleCategories_whenGettingMatchingStocks_thenReturnStocksWithCategoryInList() {
    StockQuery stockQuery = new StockQuery(null, null,
        null, Arrays.asList(FIRST_STOCK_CATEGORY, THIRD_STOCK_CATEGORY));

    List<Stock> returnedStocks = stockQuery.getMatchingStocks(ALL_STOCKS);

    assertThat(returnedStocks, containsInAnyOrder(firstStock, thirdStock));
  }
}
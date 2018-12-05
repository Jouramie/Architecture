package ca.ulaval.glo4003.domain.stock.query;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.util.TestStockBuilder;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class StockQueryTest {
  private static final String FIRST_STOCK_NAME = "firstName";
  private static final String SECOND_STOCK_NAME = "secondName";
  private static final String THIRD_STOCK_NAME = "thirdName";
  private static final String FIRST_STOCK_CATEGORY = "firstCategory";
  private static final String SECOND_STOCK_CATEGORY = "secondCategory";
  private static final String THIRD_STOCK_CATEGORY = "thirdCategory";

  private static final Stock firstStock = new TestStockBuilder()
      .withName(FIRST_STOCK_NAME).withCategory(FIRST_STOCK_CATEGORY).build();
  private static final Stock secondStock = new TestStockBuilder()
      .withName(SECOND_STOCK_NAME).withCategory(SECOND_STOCK_CATEGORY).build();
  private static final Stock thirdStock = new TestStockBuilder()
      .withName(THIRD_STOCK_NAME).withCategory(THIRD_STOCK_CATEGORY).build();
  private final List<Stock> ALL_STOCKS = Arrays.asList(firstStock, secondStock, thirdStock);

  @Test
  public void givenQueryWithNullNameList_whenGettingMatchingStocks_thenReturnAllStocks() {
    StockQuery stockQuery = new StockQuery(
        null, Arrays.asList(FIRST_STOCK_CATEGORY, SECOND_STOCK_CATEGORY, THIRD_STOCK_CATEGORY));

    List<Stock> filteredStocks = ALL_STOCKS.stream().filter(stockQuery).collect(toList());

    assertThat(filteredStocks, containsInAnyOrder(ALL_STOCKS.toArray()));
  }

  @Test
  public void givenQueryWithNullCategoryList_whenGettingMatchingStocks_thenReturnAllStocks() {
    StockQuery stockQuery = new StockQuery(
        Arrays.asList(FIRST_STOCK_NAME, SECOND_STOCK_NAME, THIRD_STOCK_NAME), null);

    List<Stock> filteredStocks = ALL_STOCKS.stream().filter(stockQuery).collect(toList());

    assertThat(filteredStocks, containsInAnyOrder(ALL_STOCKS.toArray()));
  }

  @Test
  public void givenQueryWithMultipleNames_whenGettingMatchingStocks_thenReturnStocksWithNameInList() {
    StockQuery stockQuery = new StockQuery(Arrays.asList(THIRD_STOCK_NAME, FIRST_STOCK_NAME), null);

    List<Stock> filteredStocks = ALL_STOCKS.stream().filter(stockQuery).collect(toList());

    assertThat(filteredStocks, containsInAnyOrder(firstStock, thirdStock));
  }

  @Test
  public void givenQueryWithMultipleCategories_whenGettingMatchingStocks_thenReturnStocksWithCategoryInList() {
    StockQuery stockQuery = new StockQuery(null, Arrays.asList(FIRST_STOCK_CATEGORY, THIRD_STOCK_CATEGORY));

    List<Stock> filteredStocks = ALL_STOCKS.stream().filter(stockQuery).collect(toList());

    assertThat(filteredStocks, containsInAnyOrder(firstStock, thirdStock));
  }
}
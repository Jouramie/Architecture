package ca.ulaval.glo4003.domain.stock.query;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StockQueryBuilderTest {
  private StockQueryBuilder stockQueryBuilder;

  @Before
  public void setupStockQueryBuilder() {
    stockQueryBuilder = new StockQueryBuilder();
  }

  @Test
  public void givenNameListIsNotSet_whenBuilding_thenQueryNameListIsNull() {
    StockQuery stockQuery = stockQueryBuilder.build();

    assertThat(stockQuery.getNames()).isNull();
  }

  @Test
  public void givenNameListIsSet_whenBuilding_thenQueryNameListIsGivenList() {
    List<String> someNames = Arrays.asList("name1", "name2");
    stockQueryBuilder = stockQueryBuilder.withNames(someNames);

    StockQuery stockQuery = stockQueryBuilder.build();

    Assert.assertThat(stockQuery.getNames(), containsInAnyOrder(someNames.toArray()));
  }

  @Test
  public void givenSingleNameIsPassed_whenBuilding_thenNameIsInQueryNameList() {
    String aName = "name";
    stockQueryBuilder = stockQueryBuilder.withName(aName);

    StockQuery stockQuery = stockQueryBuilder.build();

    Assert.assertThat(stockQuery.getNames(), containsInAnyOrder(aName));
  }

  @Test
  public void givenCategoryListIsNotSet_whenBuilding_thenQueryCategoryListIsNull() {
    StockQuery stockQuery = stockQueryBuilder.build();

    assertThat(stockQuery.getCategories()).isNull();
  }

  @Test
  public void givenCategoryListIsSet_whenBuilding_thenQueryCategoryListIsGivenList() {
    List<String> someCategories = Arrays.asList("category1", "category2");
    stockQueryBuilder = stockQueryBuilder.withCategories(someCategories);

    StockQuery stockQuery = stockQueryBuilder.build();

    Assert.assertThat(stockQuery.getCategories(), containsInAnyOrder(someCategories.toArray()));
  }

  @Test
  public void givenSingleCategoryIsPassed_whenBuilding_thenCategoryIsInQueryCategoryList() {
    String aCategory = "category";
    stockQueryBuilder = stockQueryBuilder.withCategory(aCategory);

    StockQuery stockQuery = stockQueryBuilder.build();

    Assert.assertThat(stockQuery.getCategories(), containsInAnyOrder(aCategory));
  }
}
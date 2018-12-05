package ca.ulaval.glo4003.domain.stock.query;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.Before;
import org.junit.Test;

public class StockQueryByNameAndCategoryBuilderTest {
  private StockQueryByNameAndCategoryBuilder stockQueryBuilder;

  @Before
  public void setupStockQueryBuilder() {
    stockQueryBuilder = new StockQueryByNameAndCategoryBuilder();
  }

  @Test
  public void givenNameIsNotSet_whenBuilding_thenQueryNameIsNull() {
    StockQueryByNameAndCategory stockQuery = stockQueryBuilder.build();

    assertThat(stockQuery.getName()).isNull();
  }

  @Test
  public void givenNameIsSet_whenBuilding_thenQueryNameIsGivenName() {
    String aName = "name";
    stockQueryBuilder = stockQueryBuilder.withName(aName);

    StockQueryByNameAndCategory stockQuery = stockQueryBuilder.build();

    assertThat(stockQuery.getName()).isEqualTo(aName);
  }

  @Test
  public void givenCategoryIsNotSet_whenBuilding_thenQueryCategoryIsNull() {
    StockQueryByNameAndCategory stockQuery = stockQueryBuilder.build();

    assertThat(stockQuery.getCategory()).isNull();
  }

  @Test
  public void givenCategoryIsSet_whenBuilding_thenQueryCategoryIsGivenCategory() {
    String aCategory = "category";
    stockQueryBuilder = stockQueryBuilder.withCategory(aCategory);

    StockQueryByNameAndCategory stockQuery = stockQueryBuilder.build();

    assertThat(stockQuery.getCategory()).isEqualTo(aCategory);
  }
}

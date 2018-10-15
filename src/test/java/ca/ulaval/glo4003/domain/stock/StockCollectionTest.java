package ca.ulaval.glo4003.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class StockCollectionTest {
  private final String SOME_TITLE = "MSFT";
  private final int SOME_QUANTITY = 3;
  private final String SOME_OTHER_TITLE = "RBS.1";
  private final int SOME_OTHER_QUANTITY = 7;

  private StockCollection stockCollection;

  @Before
  public void setupStockCollection() {
    stockCollection = new StockCollection();
  }

  @Test
  public void givenStockInCollection_whenCheckIfContains_thenReturnTrue() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(stockCollection.contains(SOME_TITLE)).isTrue();
  }

  @Test
  public void givenStockNotInCollection_whenCheckIfContains_thenReturnFalse() {
    assertThat(stockCollection.contains(SOME_TITLE)).isFalse();
  }

  @Test
  public void givenEmptyCollection_whenAddStock_thenStockQuantityCanBeRetrieved() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(stockCollection.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenStockAlreadyInCollection_whenAddStock_thenNumberOfStockIsIncreasedByQuantity() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);

    stockCollection.add(SOME_TITLE, 1);

    assertThat(stockCollection.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY + 1);
  }

  @Test
  public void givenStockInCollection_whenGetQuantity_thenQuantityOfTitleReturned() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(stockCollection.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenStockNotInCollection_whenGetQuantity_thenQuantityIsZero() {
    assertThat(stockCollection.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenStockInCollection_whenUpdate_thenQuantityIsChanged() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);

    stockCollection.update(SOME_TITLE, SOME_OTHER_QUANTITY);

    assertThat(stockCollection.getQuantity(SOME_TITLE)).isEqualTo(SOME_OTHER_QUANTITY);
  }

  @Test
  public void givenStocksInCollection_whenGetStocks_thenReturnAllStocks() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);
    stockCollection.add(SOME_OTHER_TITLE, SOME_OTHER_QUANTITY);

    assertThat(stockCollection.getStocks()).contains(SOME_TITLE, SOME_OTHER_TITLE);
  }

  @Test
  public void givenStockInCollection_whenRemoveStock_thenStockQuantityIsDecreased() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);

    stockCollection.remove(SOME_TITLE, 1);

    assertThat(stockCollection.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY - 1);
  }

  @Test
  public void givenStockInCollection_whenRemoveAll_thenQuantityIsZero() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);

    stockCollection.removeAll(SOME_TITLE);

    assertThat(stockCollection.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenCollectionIsEmpty_whenCheckIfEmpty_thenReturnTrue() {
    assertThat(stockCollection.isEmpty()).isTrue();
  }

  @Test
  public void givenCollectionIsNotEmpty_whenCheckIfEmpty_thenReturnFalse() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(stockCollection.isEmpty()).isFalse();
  }

  @Test
  public void givenCollectionIsNotEmpty_whenEmpty_thenCollectionIsEmpty() {
    stockCollection.add(SOME_TITLE, SOME_QUANTITY);

    stockCollection.empty();

    assertThat(stockCollection.isEmpty()).isTrue();
  }
}

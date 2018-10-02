package ca.ulaval.glo4003.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

public class CartTest {
  private final String SOME_TITLE = "MSFT";
  private final int SOME_QUANTITY = 3;
  private final String SOME_OTHER_TITLE = "AAPL";
  private final int SOME_OTHER_QUANTITY = 2;

  private Cart emptyCart;
  private Cart cartWithTwoItems;

  @Before
  public void setupCarts() {
    emptyCart = new Cart();

    cartWithTwoItems = new Cart();
    cartWithTwoItems.add(SOME_TITLE, SOME_QUANTITY);
    cartWithTwoItems.add(SOME_OTHER_TITLE, SOME_OTHER_QUANTITY);
  }

  @Test
  public void givenEmptyCart_whenAdd_thenItemsAreAddedToTheCart() {
    emptyCart.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(emptyCart.getQty(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenCartWithStocks_whenAddStocksAlreadyThere_thenPerformAddition() {
    cartWithTwoItems.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(cartWithTwoItems.getQty(SOME_TITLE)).isEqualTo(SOME_QUANTITY * 2);
  }

  @Test
  public void givenEmptyCart_whenAddStockWithNoQuantity_thenItemIsNotAdded() {
    emptyCart.add(SOME_TITLE, 0);

    assertThat(emptyCart.getQty(SOME_TITLE)).isEqualTo(0);
    assertThat(emptyCart.getItems()).isEmpty();
  }

  @Test
  public void givenCartWithStocks_whenRemove_thenSetStockQtyToZero() {
    cartWithTwoItems.remove(SOME_TITLE);

    assertThat(cartWithTwoItems.getQty(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenEmptyCart_whenRemove_thenDoNothing() {
    emptyCart.remove(SOME_TITLE);

    assertThat(emptyCart.getQty(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenCartWithStocks_whenEmpty_thenRemoveAllStocks() {
    cartWithTwoItems.empty();

    assertThat(cartWithTwoItems.getItems()).isEmpty();
  }

  @Test
  public void givenEmptyCart_whenEmpty_thenDoNothing() {
    emptyCart.empty();

    assertThat(emptyCart.getItems()).isEmpty();
  }

  @Test
  public void givenCartWithStocks_whenIsEmpty_thenReturnFalse() {
    assertThat(cartWithTwoItems.isEmpty()).isFalse();
  }

  @Test
  public void givenEmptyCart_whenIsEmpty_thenReturnTrue() {
    assertThat(emptyCart.isEmpty()).isTrue();
  }

  @Test
  public void givenCartWithStocks_whenGetItems_thenReturnCollectionOfItems() {
    Collection<CartItem> items = cartWithTwoItems.getItems();

    assertThat(items).contains(
        new CartItem(SOME_TITLE, SOME_QUANTITY),
        new CartItem(SOME_OTHER_TITLE, SOME_OTHER_QUANTITY));
  }
}

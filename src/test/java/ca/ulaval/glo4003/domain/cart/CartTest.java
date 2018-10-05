package ca.ulaval.glo4003.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;

public class CartTest {
  private static final String SOME_TITLE = "MSFT";
  private static final int SOME_QUANTITY = 3;
  private static final String SOME_OTHER_TITLE = "AAPL";
  private static final int SOME_OTHER_QUANTITY = 2;

  private Cart cart;

  @Before
  public void setupCarts() {
    cart = new Cart();
  }

  @Test
  public void whenAdd_thenItemsAreAddedToTheCart() {
    cart.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenCartWithStocks_whenAddStocksAlreadyThere_thenPerformAddition() {
    givenTwoItemInCart();

    cart.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY * 2);
  }

  @Test
  public void whenAddStockWithNoQuantity_thenItemIsNotAdded() {
    cart.add(SOME_TITLE, 0);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(0);
    assertThat(cart.getItems()).isEmpty();
  }

  @Test
  public void givenCartWithStocks_whenRemove_thenSetStockQtyToZero() {
    givenTwoItemInCart();

    cart.remove(SOME_TITLE);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void whenRemove_thenDoNothing() {
    cart.remove(SOME_TITLE);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenCartWithStocks_whenEmpty_thenRemoveAllStocks() {
    givenTwoItemInCart();

    cart.empty();

    assertThat(cart.getItems()).isEmpty();
  }

  @Test
  public void givenCartWithStocks_whenIsEmpty_thenReturnFalse() {
    givenTwoItemInCart();

    boolean cartIsEmpty = cart.isEmpty();

    assertThat(cartIsEmpty).isFalse();
  }

  @Test
  public void whenIsEmpty_thenReturnTrue() {
    boolean cartIsEmpty = cart.isEmpty();

    assertThat(cartIsEmpty).isTrue();
  }

  @Test
  public void givenCartWithStocks_whenGetItems_thenReturnCollectionOfItems() {
    givenTwoItemInCart();

    Collection<CartItem> items = cart.getItems();

    assertThat(items).contains(
        new CartItem(SOME_TITLE, SOME_QUANTITY),
        new CartItem(SOME_OTHER_TITLE, SOME_OTHER_QUANTITY));
  }

  @Test
  public void whenUpdate_thenStockNotInCartExceptionIsThrown() {
    ThrowingCallable update = () -> cart.update(SOME_TITLE, SOME_QUANTITY);

    assertThatThrownBy(update).isInstanceOf(StockNotInCartException.class);
  }

  @Test
  public void givenCartWithStocks_whenUpdateStocksAlreadyThere_thenUpdateQuantity() {
    givenTwoItemInCart();

    cart.update(SOME_TITLE, SOME_OTHER_QUANTITY);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(SOME_OTHER_QUANTITY);
  }

  private void givenTwoItemInCart() {
    cart.add(SOME_TITLE, SOME_QUANTITY);
    cart.add(SOME_OTHER_TITLE, SOME_OTHER_QUANTITY);
  }
}

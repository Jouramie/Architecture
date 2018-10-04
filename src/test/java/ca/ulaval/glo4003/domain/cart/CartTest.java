package ca.ulaval.glo4003.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;

public class CartTest {
  private final String SOME_TITLE = "MSFT";
  private final int SOME_QUANTITY = 3;
  private final String SOME_OTHER_TITLE = "AAPL";
  private final int SOME_OTHER_QUANTITY = 2;

  private Cart emptyCart;
  private Cart cart;

  @Before
  public void setupCarts() {
    emptyCart = new Cart();
    cart = new Cart();
  }

  @Test
  public void givenEmptyCart_whenAdd_thenItemsAreAddedToTheCart() {
    emptyCart.add(SOME_TITLE, SOME_QUANTITY);

    assertThat(emptyCart.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenCartWithStocks_whenAddStocksAlreadyThere_thenPerformAddition() {
    addTwoItemInCart();
    cart.add(SOME_TITLE, SOME_QUANTITY);
    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY * 2);
  }

  @Test
  public void givenEmptyCart_whenAddStockWithNoQuantity_thenItemIsNotAdded() {
    emptyCart.add(SOME_TITLE, 0);

    assertThat(emptyCart.getQuantity(SOME_TITLE)).isEqualTo(0);
    assertThat(emptyCart.getItems()).isEmpty();
  }

  @Test
  public void givenCartWithStocks_whenRemove_thenSetStockQtyToZero() {
    cart.remove(SOME_TITLE);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenEmptyCart_whenRemove_thenDoNothing() {
    emptyCart.remove(SOME_TITLE);

    assertThat(emptyCart.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenCartWithStocks_whenEmpty_thenRemoveAllStocks() {
    cart.empty();

    assertThat(cart.getItems()).isEmpty();
  }

  @Test
  public void givenEmptyCart_whenEmpty_thenDoNothing() {
    emptyCart.empty();

    assertThat(emptyCart.getItems()).isEmpty();
  }

  @Test
  public void givenCartWithStocks_whenIsEmpty_thenReturnFalse() {
    addTwoItemInCart();
    assertThat(cart.isEmpty()).isFalse();
  }

  @Test
  public void givenEmptyCart_whenIsEmpty_thenReturnTrue() {
    assertThat(emptyCart.isEmpty()).isTrue();
  }

  @Test
  public void givenCartWithStocks_whenGetItems_thenReturnCollectionOfItems() {
    addTwoItemInCart();
    Collection<CartItem> items = cart.getItems();

    assertThat(items).contains(
        new CartItem(SOME_TITLE, SOME_QUANTITY),
        new CartItem(SOME_OTHER_TITLE, SOME_OTHER_QUANTITY));
  }

  @Test
  public void givenEmptyCart_whenUpdate_thenStockNotInCartExceptionIsThrown() {
    ThrowableAssert.ThrowingCallable update
        = () -> emptyCart.update(SOME_TITLE, SOME_QUANTITY);

    assertThatThrownBy(update).isInstanceOf(StockNotInCartException.class);
  }

  @Test
  public void givenCartWithStocks_whenUpdateStocksAlreadyThere_thenUpdateQuantity() {
    addTwoItemInCart();
    cart.update(SOME_TITLE, SOME_OTHER_QUANTITY);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(SOME_OTHER_QUANTITY);
  }

  private void addTwoItemInCart() {
    cart.add(SOME_TITLE, SOME_QUANTITY);
    cart.add(SOME_OTHER_TITLE, SOME_OTHER_QUANTITY);
  }
}

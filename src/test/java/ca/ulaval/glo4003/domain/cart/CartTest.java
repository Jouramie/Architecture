package ca.ulaval.glo4003.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CartTest {
  private static final String SOME_TITLE = "MSFT";
  private static final int SOME_QUANTITY = 3;
  private static final String SOME_OTHER_TITLE = "AAPL";
  private static final int SOME_OTHER_QUANTITY = 2;

  @Mock
  private StockRepository someStockRepository;

  private Cart cart;

  @Before
  public void setupCarts() {
    cart = new Cart();

    given(someStockRepository.exists(SOME_TITLE)).willReturn(true);
    given(someStockRepository.exists(SOME_OTHER_TITLE)).willReturn(true);
  }

  @Test
  public void whenAdd_thenStocksAreAddedToTheCart() {
    cart.add(SOME_TITLE, SOME_QUANTITY, someStockRepository);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY);
  }

  @Test
  public void givenCartWithStocks_whenAddStocksAlreadyThere_thenPerformAddition() {
    givenTwoStocksInCart();

    cart.add(SOME_TITLE, SOME_QUANTITY, someStockRepository);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(SOME_QUANTITY * 2);
  }

  @Test
  public void whenAddStockWithNoQuantity_thenStockIsNotAdded() {
    cart.add(SOME_TITLE, 0, someStockRepository);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(0);
    assertThat(cart.getStocks().isEmpty()).isTrue();
  }

  @Test
  public void givenCartWithStocks_whenRemoveAll_thenSetStockQtyToZero() {
    givenTwoStocksInCart();

    cart.removeAll(SOME_TITLE);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void whenRemoveAll_thenDoNothing() {
    cart.removeAll(SOME_TITLE);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(0);
  }

  @Test
  public void givenCartWithStocks_whenEmpty_thenRemoveAllStocks() {
    givenTwoStocksInCart();

    cart.empty();

    assertThat(cart.getStocks().isEmpty()).isTrue();
  }

  @Test
  public void givenCartWithStocks_whenIsEmpty_thenReturnFalse() {
    givenTwoStocksInCart();

    boolean cartIsEmpty = cart.isEmpty();

    assertThat(cartIsEmpty).isFalse();
  }

  @Test
  public void whenIsEmpty_thenReturnTrue() {
    boolean cartIsEmpty = cart.isEmpty();

    assertThat(cartIsEmpty).isTrue();
  }

  @Test
  public void givenCartWithStocks_whenGetStocks_thenStockCollectionContainsStocks() {
    givenTwoStocksInCart();

    StockCollection stocks = cart.getStocks();

    assertThat(stocks.getTitles()).contains(SOME_TITLE, SOME_OTHER_TITLE);
  }

  @Test
  public void whenUpdate_thenIllegalArgumentExceptionIsThrown() {
    ThrowingCallable update = () -> cart.update(SOME_TITLE, SOME_QUANTITY);

    assertThatThrownBy(update).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void givenCartWithStocks_whenUpdateStocksAlreadyThere_thenUpdateQuantity() {
    givenTwoStocksInCart();

    cart.update(SOME_TITLE, SOME_OTHER_QUANTITY);

    assertThat(cart.getQuantity(SOME_TITLE)).isEqualTo(SOME_OTHER_QUANTITY);
  }

  private void givenTwoStocksInCart() {
    cart.add(SOME_TITLE, SOME_QUANTITY, someStockRepository);
    cart.add(SOME_OTHER_TITLE, SOME_OTHER_QUANTITY, someStockRepository);
  }
}

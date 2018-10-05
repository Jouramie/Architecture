package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.doThrow;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.cart.CartItem;
import ca.ulaval.glo4003.domain.cart.StockNotInCartException;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.user.CurrentUserRepository;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {
  private static final String SOME_TITLE = "title";
  private static final int SOME_QUANTITY = 1;
  @Mock
  private CurrentUserRepository currentUserRepository;
  @Mock
  private StockRepository stockRepository;
  @Mock
  private CartStockItemAssembler cartItemAssembler;
  @Mock
  private User currentUser;
  @Mock
  private Cart cart;
  @Mock
  private CartItem cartItem;
  @Mock
  private CartItemResponseDto cartItemDto;


  private CartService cartService;

  @Before
  public void setup() {
    given(currentUserRepository.getCurrentUser()).willReturn(currentUser);
    given(currentUser.getCart()).willReturn(cart);
    given(cart.getItems()).willReturn(new ArrayList<>());
    given(stockRepository.doesStockExist(SOME_TITLE)).willReturn(true);

    cartService = new CartService(stockRepository, currentUserRepository, cartItemAssembler);
  }

  @Test
  public void whenGetCartContent_thenCartOfTheCurrentUserIsGot() {
    cartService.getCartContent();

    verify(currentUserRepository).getCurrentUser();
    verify(currentUser).getCart();
  }

  @Test
  public void whenGetCartContent_thenWeHaveCorrespondingDtos() {
    List<CartItem> cartItems = Collections.singletonList(cartItem);
    List<CartItemResponseDto> cartItemDtos = Collections.singletonList(cartItemDto);
    given(cart.getItems()).willReturn(cartItems);
    given(cartItemAssembler.toDtoList(cartItems)).willReturn(cartItemDtos);

    List<CartItemResponseDto> resultingDtos = cartService.getCartContent();

    assertThat(resultingDtos.get(0)).isEqualTo(cartItemDto);
  }

  @Test
  public void whenAddStockToCart_thenStockIsAddedToTheCartOfTheCurrentUser() {
    cartService.addStockToCart(SOME_TITLE, SOME_QUANTITY);

    verify(currentUserRepository).getCurrentUser();
    verify(currentUser).getCart();
  }

  @Test
  public void whenAddStockToCart_thenStockIsAddedToCart() {
    cartService.addStockToCart(SOME_TITLE, SOME_QUANTITY);

    verify(cart).add(SOME_TITLE, SOME_QUANTITY);
  }

  @Test
  public void givenInvalidStockTitle_whenAddStockToCart_thenInvalidStockTitleException() {
    String invalidTitle = "invalid title";
    given(stockRepository.getByTitle(invalidTitle)).willThrow(new StockNotFoundException(invalidTitle));

    ThrowableAssert.ThrowingCallable addStockToCart
        = () -> cartService.addStockToCart(invalidTitle, SOME_QUANTITY);

    assertThatThrownBy(addStockToCart).isInstanceOf(InvalidStockTitleException.class);
  }

  @Test
  public void givenInvalidStockQuantity_whenAddStockToCart_thenInvalidStockQuantityException() {
    int invalidQuantity = -1;

    ThrowableAssert.ThrowingCallable addStockToCart
        = () -> cartService.addStockToCart(SOME_TITLE, invalidQuantity);

    assertThatThrownBy(addStockToCart).isInstanceOf(InvalidStockQuantityException.class);
  }

  @Test
  public void whenUpdateStockQuantityInCart_thenStockIsUpdatedForTheCartOfTheCurrentUser() {
    cartService.updateStockInCart(SOME_TITLE, SOME_QUANTITY);

    verify(currentUserRepository).getCurrentUser();
    verify(currentUser).getCart();
  }

  @Test
  public void whenUpdateStockQuantityInCart_thenStockIsUpdated() {
    cartService.updateStockInCart(SOME_TITLE, SOME_QUANTITY);

    verify(cart).update(SOME_TITLE, SOME_QUANTITY);
  }

  @Test
  public void givenInvalidStockTitle_whenUpdateStockQuantityInCart_thenInvalidStockTitleException() {
    String invalidTitle = "invalid title";
    given(stockRepository.getByTitle(invalidTitle)).willThrow(new StockNotFoundException(invalidTitle));

    ThrowableAssert.ThrowingCallable updateStockInCart
        = () -> cartService.updateStockInCart(invalidTitle, SOME_QUANTITY);

    assertThatThrownBy(updateStockInCart).isInstanceOf(InvalidStockTitleException.class);
  }

  @Test
  public void givenInvalidStockQuantity_whenUpdateStockQuantityInCart_thenInvalidStockQuantityException() {
    int invalidQuantity = -1;

    ThrowableAssert.ThrowingCallable updateStockInCart
        = () -> cartService.updateStockInCart(SOME_TITLE, invalidQuantity);

    assertThatThrownBy(updateStockInCart).isInstanceOf(InvalidStockQuantityException.class);
  }

  @Test
  public void givenStockTitleNotInCart_whenUpdateStockQuantityInCart_thenStockNotInCartException() {
    String notInCartTitle = "stock not in cart";
    doThrow(new StockNotInCartException(notInCartTitle))
        .when(cart).update(notInCartTitle, SOME_QUANTITY);
    given(stockRepository.doesStockExist(notInCartTitle)).willReturn(true);

    ThrowableAssert.ThrowingCallable updateStockInCart
        = () -> cartService.updateStockInCart(notInCartTitle, SOME_QUANTITY);

    assertThatThrownBy(updateStockInCart).isInstanceOf(StockNotInCartException.class);
  }

  @Test
  public void whenRemoveStockFromCart_thenStockIsRemovedFromTheCartOfTheCurrentUser() {
    cartService.removeStockFromCart(SOME_TITLE);

    verify(currentUserRepository).getCurrentUser();
    verify(currentUser).getCart();
  }

  @Test
  public void whenRemoveStockFromCart_thenStockIsRemoved() {
    cartService.removeStockFromCart(SOME_TITLE);

    verify(cart).remove(SOME_TITLE);
  }

  @Test
  public void givenInvalidStockTitle_whenRemoveStockFromCart_thenInvalidStockTitleException() {
    String invalidTitle = "invalid title";
    given(stockRepository.getByTitle(invalidTitle)).willThrow(new StockNotFoundException(invalidTitle));

    ThrowableAssert.ThrowingCallable updateStockInCart
        = () -> cartService.removeStockFromCart(invalidTitle);

    assertThatThrownBy(updateStockInCart).isInstanceOf(InvalidStockTitleException.class);
  }

  @Test
  public void whenEmptyCart_thenCartOfTheCurrentUserIsEmpty() {
    cartService.emptyCart();

    verify(currentUserRepository).getCurrentUser();
    verify(currentUser).getCart();
  }

  @Test
  public void whenEmptyCart_thenCartIsEmpty() {
    cartService.emptyCart();

    verify(cart).empty();
  }
}

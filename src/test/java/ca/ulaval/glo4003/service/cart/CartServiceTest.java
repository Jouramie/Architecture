package ca.ulaval.glo4003.service.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.User;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exceptions.UserNotFoundException;
import ca.ulaval.glo4003.service.cart.assemblers.CartItemAssembler;
import ca.ulaval.glo4003.service.cart.dto.CartItemDto;
import ca.ulaval.glo4003.service.cart.exceptions.InvalidStockTitleException;
import ca.ulaval.glo4003.service.cart.exceptions.StockNotInCartException;
import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
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
  private CurrentUserSession currentUserSession;
  @Mock
  private UserRepository userRepository;
  @Mock
  private StockRepository stockRepository;
  @Mock
  private CartItemAssembler cartItemAssembler;
  @Mock
  private User currentUser;
  @Mock
  private Cart cart;
  @Mock
  private CartItemDto cartItemDto;

  private CartService cartService;

  @Before
  public void setup() {
    given(currentUserSession.getCurrentUser()).willReturn(currentUser);
    given(currentUser.getCart()).willReturn(cart);
    given(stockRepository.exists(SOME_TITLE)).willReturn(true);

    cartService = new CartService(stockRepository, currentUserSession, userRepository, cartItemAssembler);
  }

  @Test
  public void whenGetCartContent_thenCartOfTheCurrentUserIsGot() {
    List<CartItemDto> content = cartService.getCartContent();

    assertThat(content).hasSize(0);
  }

  @Test
  public void whenGetCartContent_thenWeHaveCorrespondingDtos() {
    StockCollection stockCollection = new StockCollection();
    List<CartItemDto> cartItemDtos = Collections.singletonList(cartItemDto);
    given(cart.getStocks()).willReturn(stockCollection);
    given(cartItemAssembler.toDtoList(stockCollection)).willReturn(cartItemDtos);

    List<CartItemDto> resultingDtos = cartService.getCartContent();

    assertThat(resultingDtos.get(0)).isEqualTo(cartItemDto);
  }

  @Test
  public void givenOneStockOfCartDoesNotExist_whenGetCartContent_thenInvalidStockExceptionIsThrown() {
    doThrow(StockNotFoundException.class).when(cartItemAssembler).toDtoList(any());

    assertThatThrownBy(() -> cartService.getCartContent());
  }

  @Test
  public void whenAddStockToCart_thenStockIsAddedToCart() throws UserNotFoundException {
    cartService.addStockToCart(SOME_TITLE, SOME_QUANTITY);

    verify(cart).add(SOME_TITLE, SOME_QUANTITY, stockRepository);
    verify(userRepository).update(currentUser);
  }

  @Test
  public void givenInvalidStockTitle_whenAddStockToCart_thenInvalidStockTitleException()
      throws StockNotFoundException {
    String invalidTitle = "invalid title";
    given(stockRepository.findByTitle(invalidTitle)).willThrow(new StockNotFoundException(invalidTitle));

    ThrowableAssert.ThrowingCallable addStockToCart
        = () -> cartService.addStockToCart(invalidTitle, SOME_QUANTITY);

    assertThatThrownBy(addStockToCart).isInstanceOf(InvalidStockTitleException.class);
  }

  @Test
  public void whenUpdateStockQuantityInCart_thenStockIsUpdated()
      throws UserNotFoundException {
    cartService.updateStockInCart(SOME_TITLE, SOME_QUANTITY);

    verify(cart).update(SOME_TITLE, SOME_QUANTITY);
    verify(userRepository).update(currentUser);
  }

  @Test
  public void givenInvalidStockTitle_whenUpdateStockQuantityInCart_thenInvalidStockTitleException()
      throws StockNotFoundException {
    String invalidTitle = "invalid title";
    given(stockRepository.findByTitle(invalidTitle)).willThrow(new StockNotFoundException(invalidTitle));

    ThrowableAssert.ThrowingCallable updateStockInCart
        = () -> cartService.updateStockInCart(invalidTitle, SOME_QUANTITY);

    assertThatThrownBy(updateStockInCart).isInstanceOf(InvalidStockTitleException.class);
  }

  @Test
  public void givenStockTitleNotInCart_whenUpdateStockQuantityInCart_thenIllegalArgumentException() {
    String notInCartTitle = "stock not in cart";
    doThrow(new IllegalArgumentException())
        .when(cart).update(notInCartTitle, SOME_QUANTITY);
    given(stockRepository.exists(notInCartTitle)).willReturn(true);

    ThrowableAssert.ThrowingCallable updateStockInCart
        = () -> cartService.updateStockInCart(notInCartTitle, SOME_QUANTITY);

    assertThatThrownBy(updateStockInCart).isInstanceOf(StockNotInCartException.class);
  }

  @Test
  public void whenRemoveStockFromCart_thenStockIsRemoved() throws UserNotFoundException {
    cartService.removeStockFromCart(SOME_TITLE);

    verify(cart).removeAll(SOME_TITLE);
    verify(userRepository).update(currentUser);
  }

  @Test
  public void givenInvalidStockTitle_whenRemoveStockFromCart_thenInvalidStockTitleException()
      throws StockNotFoundException {
    String invalidTitle = "invalid title";
    given(stockRepository.findByTitle(invalidTitle)).willThrow(new StockNotFoundException(invalidTitle));

    ThrowableAssert.ThrowingCallable updateStockInCart
        = () -> cartService.removeStockFromCart(invalidTitle);

    assertThatThrownBy(updateStockInCart).isInstanceOf(InvalidStockTitleException.class);
  }

  @Test
  public void whenEmptyCart_thenCartIsEmpty() throws UserNotFoundException {
    cartService.emptyCart();

    verify(cart).empty();
    verify(userRepository).update(currentUser);
  }

  @Test
  public void givenCurrentUserDoesNotExist_whenAddingStockToCart_thenUserDoesNotExistExceptionIsThrown()
      throws UserNotFoundException {
    doThrow(UserNotFoundException.class).when(userRepository).update(any());

    assertThatThrownBy(() -> cartService.addStockToCart(SOME_TITLE, SOME_QUANTITY))
        .isInstanceOf(UserDoesNotExistException.class);
  }
}

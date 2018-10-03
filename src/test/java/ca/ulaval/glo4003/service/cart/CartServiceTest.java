package ca.ulaval.glo4003.service.cart;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.cart.CartItem;
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

    cartService = new CartService(currentUserRepository, stockRepository, cartItemAssembler);
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
}

package ca.ulaval.glo4003.ws.api;

import static ca.ulaval.glo4003.util.InputValidationTestUtil.assertThatExceptionContainsErrorFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4003.cart.CartStockRequestBuilder;
import ca.ulaval.glo4003.service.cart.CartService;
import ca.ulaval.glo4003.service.cart.CheckoutService;
import ca.ulaval.glo4003.service.cart.dto.CartItemDto;
import ca.ulaval.glo4003.service.cart.dto.TransactionDto;
import ca.ulaval.glo4003.ws.api.cart.CartResourceImpl;
import ca.ulaval.glo4003.ws.api.cart.assemblers.ApiCartItemAssembler;
import ca.ulaval.glo4003.ws.api.cart.assemblers.ApiTransactionAssembler;
import ca.ulaval.glo4003.ws.api.cart.dto.ApiCartItemResponseDto;
import ca.ulaval.glo4003.ws.api.cart.dto.ApiTransactionDto;
import ca.ulaval.glo4003.ws.api.cart.dto.CartStockRequestDto;
import ca.ulaval.glo4003.ws.api.validation.InvalidInputException;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CartResourceTest {
  private static final String SOME_TITLE = "title";
  private static final CartStockRequestDto SOME_CART_STOCK_REQUEST = new CartStockRequestBuilder().build();


  @Mock
  private CartItemDto serviceDto;
  @Mock
  private ApiCartItemResponseDto expectedDto;
  @Mock
  private TransactionDto transactionDto;
  @Mock
  private CartService cartService;
  @Mock
  private CheckoutService checkoutService;

  @Mock
  private ApiTransactionAssembler apiTransactionAssembler;

  @Mock
  private ApiCartItemAssembler apiCartItemAssembler;

  private CartResourceImpl cartResource;

  @Before
  public void setup() {
    given(cartService.getCartContent()).willReturn(Collections.singletonList(serviceDto));
    given(apiCartItemAssembler.toDtoList(Collections.singletonList(serviceDto)))
        .willReturn(Collections.singletonList(expectedDto));
    cartResource = new CartResourceImpl(cartService, checkoutService,
        apiTransactionAssembler, apiCartItemAssembler);
  }

  @Test
  public void whenGetCartContent_thenReturningCartContent() {
    List<ApiCartItemResponseDto> resultingDto = cartResource.getCartContent();

    assertThat(resultingDto.get(0)).isEqualTo(expectedDto);
  }

  @Test
  public void whenAddStockToCart_thenStockIsAdded() {
    cartResource.addStockToCart(SOME_TITLE, SOME_CART_STOCK_REQUEST);

    verify(cartService).addStockToCart(SOME_TITLE, SOME_CART_STOCK_REQUEST.quantity);
  }

  @Test
  public void whenAddStockToCart_thenReturnCartContent() {
    List<ApiCartItemResponseDto> resultingDto = cartResource.addStockToCart(SOME_TITLE, SOME_CART_STOCK_REQUEST);

    assertThat(resultingDto.get(0)).isEqualTo(expectedDto);
  }

  @Test
  public void givenNegativeQuantityStockRequest_whenAddStockToCart_thenInvalidInputExceptionShouldBeThrown() {
    CartStockRequestDto negativeQuantityStockRequest = new CartStockRequestDto(-1);

    Throwable thrown = catchThrowable(() -> cartResource.addStockToCart(SOME_TITLE, negativeQuantityStockRequest));

    assertThat(thrown).isInstanceOf(InvalidInputException.class);
    InvalidInputException exception = (InvalidInputException) thrown;
    assertThatExceptionContainsErrorFor(exception, "quantity");
  }

  @Test
  public void whenUpdateStockInCart_thenStockIsUpdated() {
    cartResource.updateStockInCart(SOME_TITLE, SOME_CART_STOCK_REQUEST);

    verify(cartService).updateStockInCart(SOME_TITLE, SOME_CART_STOCK_REQUEST.quantity);
  }

  @Test
  public void whenUpdateStockInCart_thenReturnCartContent() {
    List<ApiCartItemResponseDto> resultingDto = cartResource.updateStockInCart(SOME_TITLE, SOME_CART_STOCK_REQUEST);

    assertThat(resultingDto.get(0)).isEqualTo(expectedDto);
  }

  @Test
  public void givenNegativeQuantityStockRequest_whenUpdateStockToCart_thenInvalidInputExceptionShouldBeThrown() {
    CartStockRequestDto negativeQuantityStockRequest = new CartStockRequestDto(-1);

    Throwable thrown = catchThrowable(() -> cartResource.updateStockInCart(SOME_TITLE, negativeQuantityStockRequest));

    assertThat(thrown).isInstanceOf(InvalidInputException.class);
    InvalidInputException exception = (InvalidInputException) thrown;
    assertThatExceptionContainsErrorFor(exception, "quantity");
  }

  @Test
  public void whenRemoveStockFromCart_thenStockIsRemoved() {
    cartResource.deleteStockInCart(SOME_TITLE);

    verify(cartService).removeStockFromCart(SOME_TITLE);
  }

  @Test
  public void whenRemoveStockFromCart_thenReturnCartContent() {
    List<ApiCartItemResponseDto> resultingDto = cartResource.deleteStockInCart(SOME_TITLE);

    assertThat(resultingDto.get(0)).isEqualTo(expectedDto);
  }

  @Test
  public void whenEmptyingTheCart_thenCartIsEmpty() {
    cartResource.emptyCart();

    verify(cartService).emptyCart();
  }

  @Test
  public void whenCheckoutCart_thenReturnPreviousCartContent() {
    given(checkoutService.checkoutCart()).willReturn(transactionDto);

    ApiTransactionDto resultingDto = cartResource.checkoutCart();

    ApiTransactionDto assemblerResult = apiTransactionAssembler.toDto(transactionDto);
    assertThat(resultingDto).isEqualTo(assemblerResult);
  }

  @Test
  public void whenCheckoutCart_thenCheckoutProceed() {
    cartResource.checkoutCart();

    verify(checkoutService).checkoutCart();
  }
}

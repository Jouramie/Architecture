package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.service.cart.CartService;
import ca.ulaval.glo4003.service.cart.CheckoutService;
import ca.ulaval.glo4003.service.cart.TransactionDto;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class CartResourceImpl implements CartResource {
  private final CartService cartService;
  private final CheckoutService checkoutService;
  private final ApiTransactionAssembler apiTransactionAssembler;
  private final ApiCartItemAssembler apiCartItemAssembler;
  private final RequestValidator requestValidator;

  @Inject
  public CartResourceImpl(CartService cartService, CheckoutService checkoutService, ApiTransactionAssembler apiTransactionAssembler, ApiCartItemAssembler apiCartItemAssembler) {
    this.cartService = cartService;
    this.checkoutService = checkoutService;
    this.apiTransactionAssembler = apiTransactionAssembler;
    this.apiCartItemAssembler = apiCartItemAssembler;
    requestValidator = new RequestValidator();
  }

  @Override
  public List<ApiCartItemResponseDto> getCartContent() {
    return apiCartItemAssembler.toDtoList(cartService.getCartContent());
  }

  @Override
  public List<ApiCartItemResponseDto> addStockToCart(String title,
                                                     CartStockRequest cartStockRequest) {
    requestValidator.validate(cartStockRequest);
    cartService.addStockToCart(title, cartStockRequest.quantity);

    return apiCartItemAssembler.toDtoList(cartService.getCartContent());
  }

  @Override
  public List<ApiCartItemResponseDto> updateStockInCart(String title,
                                                        CartStockRequest cartStockRequest) {
    requestValidator.validate(cartStockRequest);
    cartService.updateStockInCart(title, cartStockRequest.quantity);
    return apiCartItemAssembler.toDtoList(cartService.getCartContent());
  }

  @Override
  public List<ApiCartItemResponseDto> deleteStockInCart(String title) {
    cartService.removeStockFromCart(title);
    return apiCartItemAssembler.toDtoList(cartService.getCartContent());
  }

  @Override
  public void emptyCart() {
    cartService.emptyCart();
  }

  @Override
  public ApiTransactionDto checkoutCart() {
    TransactionDto transactionDto = checkoutService.checkoutCart();
    ApiTransactionDto apiTransactionDto = apiTransactionAssembler.toDto(transactionDto);
    return apiTransactionDto;
  }
}

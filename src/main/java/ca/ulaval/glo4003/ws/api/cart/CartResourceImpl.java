package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.service.cart.CartService;
import ca.ulaval.glo4003.service.cart.CheckoutService;
import ca.ulaval.glo4003.ws.api.validation.RequestValidator;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class CartResourceImpl implements CartResource {
  private final CartService cartService;
  private final CheckoutService checkoutService;
  private final RequestValidator requestValidator;

  @Inject
  public CartResourceImpl(CartService cartService, CheckoutService checkoutService) {
    this.cartService = cartService;
    this.checkoutService = checkoutService;
    this.requestValidator = new RequestValidator();
  }

  @Override
  public List<CartItemResponseDto> getCartContent() {
    return cartService.getCartContent();
  }

  @Override
  public List<CartItemResponseDto> addStockToCart(String title,
                                                  CartStockRequest cartStockRequest) {
    requestValidator.validate(cartStockRequest);
    cartService.addStockToCart(title, cartStockRequest.quantity);
    return cartService.getCartContent();
  }

  @Override
  public List<CartItemResponseDto> updateStockInCart(String title,
                                                     CartStockRequest cartStockRequest) {
    requestValidator.validate(cartStockRequest);
    cartService.updateStockInCart(title, cartStockRequest.quantity);
    return cartService.getCartContent();
  }

  @Override
  public List<CartItemResponseDto> deleteStockInCart(String title) {
    cartService.removeStockFromCart(title);
    return cartService.getCartContent();
  }

  @Override
  public void emptyCart() {
    cartService.emptyCart();
  }

  @Override
  public TransactionDto checkoutCart() {
    return checkoutService.checkoutCart();
  }
}

package ca.ulaval.glo4003.ws.api.cart;

import ca.ulaval.glo4003.service.cart.CartService;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;

@Resource
public class CartResourceImpl implements CartResource {
  private final CartService cartService;

  @Inject
  public CartResourceImpl(CartService cartService) {
    this.cartService = cartService;
  }

  @Override
  public List<CartItemResponseDto> getCartContent() {
    return cartService.getCartContent();
  }

  @Override
  public List<CartItemResponseDto> addStockToCart(String title,
                                                  CartStockRequest cartStockRequest) {
    cartService.addStockToCart(title, cartStockRequest.quantity);
    return cartService.getCartContent();
  }

  @Override
  public List<CartItemResponseDto> updateStockInCart(String title,
                                                     CartStockRequest cartStockRequest) {
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
  public List<CartItemResponseDto> checkoutCart() {
    return null;
  }
}

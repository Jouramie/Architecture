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
    cartService.addStockToCart(cartStockRequest.title, cartStockRequest.quantity);
    return cartService.getCartContent();
  }

  @Override
  public List<CartItemResponseDto> updateStockInCart(String title,
                                                   CartStockRequest cartStockRequest) {
    return null;
  }

  @Override
  public List<CartItemResponseDto> deleteStockInCart(String title,
                                                   CartStockRequest cartStockRequest) {
    return null;
  }

  @Override
  public void emptyCart() {

  }

  @Override
  public List<CartItemResponseDto> checkoutCart() {
    return null;
  }
}

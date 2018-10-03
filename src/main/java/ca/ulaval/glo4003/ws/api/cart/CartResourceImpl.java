package ca.ulaval.glo4003.ws.api.cart;

import java.util.List;
import javax.annotation.Resource;

@Resource
public class CartResourceImpl implements CartResource {
  @Override
  public List<CartStockResponse> getCartContent() {
    return null;
  }

  @Override
  public List<CartStockResponse> addStockToCart(String title,
                                                CartStockRequest cartStockRequest) {
    return null;
  }

  @Override
  public List<CartStockResponse> updateStockInCart(String title,
                                                   CartStockRequest cartStockRequest) {
    return null;
  }

  @Override
  public List<CartStockResponse> deleteStockInCart(String title,
                                                   CartStockRequest cartStockRequest) {
    return null;
  }

  @Override
  public void emptyCart() {
  }

  @Override
  public List<CartStockResponse> checkoutCart() {
    return null;
  }
}

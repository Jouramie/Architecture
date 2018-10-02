package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.user.CurrentUserRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.util.List;
import javax.inject.Inject;

@Component
public class CartService {
  private final CurrentUserRepository currentUserRepository;
  private final StockRepository stockRepository;
  private final CartStockItemAssembler assembler;

  @Inject
  public CartService(CurrentUserRepository currentUserRepository, StockRepository stockRepository, CartStockItemAssembler assembler) {
    this.currentUserRepository = currentUserRepository;
    this.stockRepository = stockRepository;
    this.assembler = assembler;
  }

  public List<CartItemResponseDto> getCartContent() {
    Cart cart = getCart();
    return assembler.toDtoList(cart.getItems());
  }

  public void addStockToCart(String title, int quantity) {
    checkIfStockExists(title);
    checkValidQuantity(quantity);

    Cart cart = getCart();
    cart.add(title, quantity);
  }

  private Cart getCart() {
    return currentUserRepository.getCurrentUser().getCart();
  }

  private void checkIfStockExists(String title) {
    try {
      stockRepository.getByTitle(title);
    } catch (StockNotFoundException e) {
      throw new InvalidStockTitleException(title);
    }
  }

  private void checkValidQuantity(int quantity) {
    if (quantity < 0) {
      throw new InvalidStockQuantityException();
    }
  }
}

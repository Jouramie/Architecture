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
  public CartService(StockRepository stockRepository,
                     CurrentUserRepository currentUserRepository,
                     CartStockItemAssembler assembler) {
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
    checkIfValidQuantity(quantity);

    Cart cart = getCart();
    cart.add(title, quantity);
  }

  public void updateStockInCart(String title, int quantity) {
    checkIfStockExists(title);
    checkIfValidQuantity(quantity);

    Cart cart = getCart();
    cart.update(title, quantity);
  }

  public void removeStockFromCart(String title) {
    checkIfStockExists(title);

    Cart cart = getCart();
    cart.remove(title);
  }

  public void emptyCart() {
    Cart cart = getCart();
    cart.empty();
  }

  private Cart getCart() {
    return currentUserRepository.getCurrentUser().getCart();
  }

  private void checkIfStockExists(String title) {
    try {
      stockRepository.getByTitle(title);
    } catch (StockNotFoundException e) {
      throw new InvalidStockTitleException(title, e);
    }
  }

  private void checkIfValidQuantity(int quantity) {
    if (quantity <= 0) {
      throw new InvalidStockQuantityException();
    }
  }
}

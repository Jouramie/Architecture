package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.user.CurrentUserRepository;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.util.List;
import javax.inject.Inject;

@Component
public class CartService {
  private final CurrentUserRepository currentUserRepository;
  private final UserRepository userRepository;
  private final StockRepository stockRepository;
  private final CartItemAssembler assembler;

  @Inject
  public CartService(StockRepository stockRepository,
                     CurrentUserRepository currentUserRepository,
                     UserRepository userRepository,
                     CartItemAssembler assembler) {
    this.currentUserRepository = currentUserRepository;
    this.userRepository = userRepository;
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

    updateUser();
  }

  public void updateStockInCart(String title, int quantity) {
    checkIfStockExists(title);
    checkIfValidQuantity(quantity);

    Cart cart = getCart();
    cart.update(title, quantity);

    updateUser();
  }

  public void removeStockFromCart(String title) {
    checkIfStockExists(title);

    Cart cart = getCart();
    cart.remove(title);

    updateUser();
  }

  public void emptyCart() {
    Cart cart = getCart();
    cart.empty();

    updateUser();
  }

  private Cart getCart() {
    return currentUserRepository.getCurrentUser().getCart();
  }

  private void checkIfStockExists(String title) {
    if (!stockRepository.doesStockExist(title)) {
      throw new InvalidStockTitleException(title);
    }
  }

  private void checkIfValidQuantity(int quantity) {
    if (quantity <= 0) {
      throw new InvalidStockQuantityException();
    }
  }

  private void updateUser() {
    userRepository.update(currentUserRepository.getCurrentUser());
  }
}

package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.UserNotFoundException;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import ca.ulaval.glo4003.service.user.UserDoesNotExistException;
import ca.ulaval.glo4003.ws.api.cart.CartItemResponseDto;
import java.util.List;
import javax.inject.Inject;

@Component
public class CartService {
  private final CurrentUserSession currentUserSession;
  private final UserRepository userRepository;
  private final StockRepository stockRepository;
  private final CartItemAssembler assembler;

  @Inject
  public CartService(StockRepository stockRepository,
                     CurrentUserSession currentUserSession,
                     UserRepository userRepository,
                     CartItemAssembler assembler) {
    this.currentUserSession = currentUserSession;
    this.userRepository = userRepository;
    this.stockRepository = stockRepository;
    this.assembler = assembler;
  }

  public List<CartItemResponseDto> getCartContent() {
    Cart cart = getCart();
    return assembler.toDtoList(cart.getStocks());
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
    try {
      cart.update(title, quantity);
    } catch (IllegalArgumentException exception) {
      throw new StockNotInCartException(exception);
    }

    updateUser();
  }

  public void removeStockFromCart(String title) {
    checkIfStockExists(title);

    Cart cart = getCart();
    cart.removeAll(title);

    updateUser();
  }

  public void emptyCart() {
    Cart cart = getCart();
    cart.empty();

    updateUser();
  }

  private Cart getCart() {
    return currentUserSession.getCurrentUser().getCart();
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
    try {
      userRepository.update(currentUserSession.getCurrentUser());
    } catch (UserNotFoundException exception) {
      throw new UserDoesNotExistException(exception);
    }
  }
}

package ca.ulaval.glo4003.service.cart;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.user.CurrentUserSession;
import ca.ulaval.glo4003.domain.user.Investor;
import ca.ulaval.glo4003.domain.user.UserRepository;
import ca.ulaval.glo4003.domain.user.exception.UserNotFoundException;
import ca.ulaval.glo4003.service.cart.exception.InvalidStockTitleException;
import ca.ulaval.glo4003.service.cart.exception.StockNotInCartException;
import ca.ulaval.glo4003.service.user.exception.UserDoesNotExistException;
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

  public List<CartItemDto> getCartContent() {
    Cart cart = getCart();
    return assembler.toDtoList(cart.getStocks());
  }

  public void addStockToCart(String title, int quantity) {
    ensureStockExists(title);

    Cart cart = getCart();
    cart.add(title, quantity, stockRepository);

    updateUser();
  }

  public void updateStockInCart(String title, int quantity) {
    ensureStockExists(title);

    Cart cart = getCart();
    try {
      cart.update(title, quantity);
    } catch (IllegalArgumentException exception) {
      throw new StockNotInCartException(exception);
    }

    updateUser();
  }

  public void removeStockFromCart(String title) {
    ensureStockExists(title);

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
    return currentUserSession.getCurrentUser(Investor.class).getCart();
  }

  private void ensureStockExists(String title) {
    if (!stockRepository.exists(title)) {
      throw new InvalidStockTitleException(title);
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

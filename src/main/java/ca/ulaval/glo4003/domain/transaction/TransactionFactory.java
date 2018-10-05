package ca.ulaval.glo4003.domain.transaction;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.cart.CartItem;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

@Component
public class TransactionFactory {
  private final Clock clock;
  private final StockRepository stockRepository;

  @Inject
  public TransactionFactory(Clock clock, StockRepository stockRepository) {
    this.clock = clock;
    this.stockRepository = stockRepository;
  }

  public Transaction createPurchase(Cart cart) {
    List<TransactionItem> transactionItems = buildTransactionItems(cart.getItems());
    return new Transaction(clock.getCurrentTime(), transactionItems, TransactionType.PURCHASE);
  }

  private List<TransactionItem> buildTransactionItems(Collection<CartItem> cartItems) {
    return cartItems.stream().map(this::buildTransactionItem).collect(toList());
  }

  private TransactionItem buildTransactionItem(CartItem item) {
    MoneyAmount amount = stockRepository.getByTitle(item.title).getValue().getCurrentValue();
    return new TransactionItem(item.title, item.quantity, amount);
  }
}

package ca.ulaval.glo4003.infrastructure.payment;

import static java.util.stream.Collectors.toList;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.cart.CartItem;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.util.Collection;
import java.util.List;

public class TransactionFactory {
  private final Clock clock;
  private final StockRepository stockRepository;
  private MoneyAmount amount;

  public TransactionFactory(Clock clock, StockRepository stockRepository) {
    this.clock = clock;
    this.stockRepository = stockRepository;
  }

  public Transaction create(Cart cart) {
    List transactionItems = createTransactionItems(cart.getItems());
    return new Transaction(clock.getCurrentTime(), transactionItems, TransactionType.PURCHASE);
  }

  public List<TransactionItem> createTransactionItems(Collection<CartItem> cartItems) {
    return getTransactionItemsList(cartItems);
  }

  public TransactionItem getTransactionItem(CartItem item) {
    amount = stockRepository.getByTitle(item.title).getValue().getCurrentValue();
    return new TransactionItem(item.title, item.quantity, amount);
  }

  public List<TransactionItem> getTransactionItemsList(Collection<CartItem> items) {
    return items.stream().map(this::getTransactionItem).collect(toList());
  }
}

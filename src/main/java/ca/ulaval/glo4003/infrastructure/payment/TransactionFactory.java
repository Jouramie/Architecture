package ca.ulaval.glo4003.infrastructure.payment;

import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.cart.CartItem;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.util.ArrayList;
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
    return new Transaction(clock, transactionItems, TransactionType.PURCHASE);
  }

  public List<TransactionItem> createTransactionItems(Collection<CartItem> cartItems) {
    List transactionItems = new ArrayList<>();
    for (CartItem item : cartItems) {
      MoneyAmount amount = getAmount(item.title);
      transactionItems.add(new TransactionItem(item.title, item.quantity, amount));
    }
    return transactionItems;
  }

  public MoneyAmount getAmount(String stockId) {
    amount = stockRepository.getByTitle(stockId).getValue().getCurrentValue();
    return amount;
  }
}

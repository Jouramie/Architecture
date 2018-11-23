package ca.ulaval.glo4003.domain.transaction;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.cart.Cart;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.StockCollection;
import ca.ulaval.glo4003.domain.stock.StockNotFoundException;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import java.util.ArrayList;
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

  public Transaction createPurchase(Cart cart) throws StockNotFoundException {
    List<TransactionItem> transactionItems = buildTransactionItems(cart.getStocks());
    return new Transaction(clock.getCurrentTime(), transactionItems, TransactionType.PURCHASE);
  }

  private List<TransactionItem> buildTransactionItems(StockCollection items) throws StockNotFoundException {
    List<TransactionItem> transactionItems = new ArrayList<>();
    for (String title : items.getTitles()) {
      int quantity = items.getQuantity(title);
      transactionItems.add(buildTransactionItem(title, quantity));
    }
    return transactionItems;
  }

  private TransactionItem buildTransactionItem(String title, int quantity) throws StockNotFoundException {
    MoneyAmount amount = stockRepository.findByTitle(title).getValue().getLatestValue();
    return new TransactionItem(title, quantity, amount);
  }
}

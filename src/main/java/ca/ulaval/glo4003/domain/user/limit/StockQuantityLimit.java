package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import java.time.LocalDateTime;

public class StockQuantityLimit extends Limit {
  private final int stockQuantity;

  public StockQuantityLimit(LocalDateTime start, LocalDateTime end, int stockQuantity) {
    super(start, end);
    this.stockQuantity = stockQuantity;
  }

  @Override
  public boolean doesTransactionExceedLimit(Transaction transaction) {
    return isTransactionInsideLimitTimeSpan(transaction)
        && transaction.getTotalQuantity() > stockQuantity;
  }
}

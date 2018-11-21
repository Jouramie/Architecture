package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import java.time.LocalDateTime;

public class StockQuantityLimit extends TemporaryLimit {
  private final int stockQuantity;

  public StockQuantityLimit(LocalDateTime start, LocalDateTime end, int stockQuantity) {
    super(start, end);
    this.stockQuantity = stockQuantity;
  }

  @Override
  protected boolean isSpecificCriteriaExceeded(Transaction transaction) {
    return transaction.getTotalQuantity() > stockQuantity;
  }
}

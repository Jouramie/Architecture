package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.transaction.Transaction;
import java.time.LocalDateTime;

public class StockQuantityLimit extends TemporaryLimit {
  public final int stockQuantity;

  public StockQuantityLimit(LocalDateTime begin, LocalDateTime end, int stockQuantity) {
    super(begin, end);
    this.stockQuantity = stockQuantity;
  }

  @Override
  protected boolean isSpecificCriteriaExceeded(Transaction transaction) {
    return transaction.getTotalQuantity() > stockQuantity;
  }
}

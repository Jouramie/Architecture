package ca.ulaval.glo4003.domain.user.limit;

import org.joda.time.DateTime;

public class StockQuantityLimit extends Limit {
  private final int stockQuantity;

  public StockQuantityLimit(DateTime start, DateTime end, int stockQuantity) {
    super(start, end);
    this.stockQuantity = stockQuantity;
  }
}

package ca.ulaval.glo4003.domain.user.limit;

import java.time.LocalDateTime;

public class StockQuantityLimit extends Limit {
  private final int stockQuantity;

  public StockQuantityLimit(LocalDateTime start, LocalDateTime end, int stockQuantity) {
    super(start, end);
    this.stockQuantity = stockQuantity;
  }
}

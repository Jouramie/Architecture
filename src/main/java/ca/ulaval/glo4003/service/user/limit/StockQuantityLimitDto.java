package ca.ulaval.glo4003.service.user.limit;

import java.time.LocalDateTime;

public class StockQuantityLimitDto extends LimitDto {
  public final int stockQuantity;

  public StockQuantityLimitDto(LocalDateTime begin, LocalDateTime end, int stockQuantity) {
    super(begin, end);
    this.stockQuantity = stockQuantity;
  }
}

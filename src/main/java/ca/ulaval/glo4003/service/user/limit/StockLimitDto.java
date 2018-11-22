package ca.ulaval.glo4003.service.user.limit;

import java.time.LocalDateTime;

public class StockLimitDto extends LimitDto {
  public final int stockQuantity;

  public StockLimitDto(LocalDateTime begin, LocalDateTime end, int stockQuantity) {
    super(begin, end);
    this.stockQuantity = stockQuantity;
  }
}

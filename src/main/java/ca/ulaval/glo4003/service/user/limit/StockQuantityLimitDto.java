package ca.ulaval.glo4003.service.user.limit;

import java.time.LocalDateTime;

public class StockQuantityLimitDto extends LimitDto {

  public final int stockQuantity;

  public StockQuantityLimitDto(LocalDateTime from, LocalDateTime to, int stockQuantity) {
    super(from, to);
    this.stockQuantity = stockQuantity;
  }
}

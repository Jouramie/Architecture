package ca.ulaval.glo4003.service.user.limit;

import java.time.LocalDateTime;

public class StockLimitDto extends LimitDto {

  public final int stockQuantity;

  public StockLimitDto(LocalDateTime from, LocalDateTime to, int stockQuantity) {
    super(from, to);
    this.stockQuantity = stockQuantity;
  }
}

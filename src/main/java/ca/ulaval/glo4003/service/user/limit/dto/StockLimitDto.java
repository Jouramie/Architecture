package ca.ulaval.glo4003.service.user.limit.dto;

import java.time.LocalDateTime;

public class StockLimitDto extends LimitDto {
  public final int maximalStockQuantity;

  public StockLimitDto(int maximalStockQuantity, LocalDateTime start, LocalDateTime end) {
    super(start, end);
    this.maximalStockQuantity = maximalStockQuantity;
  }
}

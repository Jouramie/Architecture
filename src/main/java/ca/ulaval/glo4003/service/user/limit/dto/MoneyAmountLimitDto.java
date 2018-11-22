package ca.ulaval.glo4003.service.user.limit.dto;

import java.time.LocalDateTime;

public class MoneyAmountLimitDto extends LimitDto {

  public final double maximalMoneySpent;

  public MoneyAmountLimitDto(double maximalMoneySpent, LocalDateTime start, LocalDateTime end) {
    super(start, end);
    this.maximalMoneySpent = maximalMoneySpent;
  }
}

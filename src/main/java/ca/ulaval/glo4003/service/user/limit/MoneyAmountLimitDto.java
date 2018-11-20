package ca.ulaval.glo4003.service.user.limit;

import java.time.LocalDateTime;

public class MoneyAmountLimitDto extends LimitDto {

  public final double moneyAmount;

  public MoneyAmountLimitDto(LocalDateTime from, LocalDateTime to, double moneyAmount) {
    super(from, to);
    this.moneyAmount = moneyAmount;
  }
}

package ca.ulaval.glo4003.service.user.limit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MoneyAmountLimitDto extends LimitDto {

  public final BigDecimal moneyAmount;

  public MoneyAmountLimitDto(LocalDateTime from, LocalDateTime to, BigDecimal moneyAmount) {
    super(from, to);
    this.moneyAmount = moneyAmount;
  }
}

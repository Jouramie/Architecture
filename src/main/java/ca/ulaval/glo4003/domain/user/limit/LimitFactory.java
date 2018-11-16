package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.time.LocalDateTime;

public class LimitFactory {
  public StockQuantityLimit createStockQuantityLimit(LocalDateTime start, ApplicationPeriod applicationPeriod, int stockQuantity) {
    return new StockQuantityLimit(start, calculateEnd(start, applicationPeriod), stockQuantity);
  }

  public MoneyAmountLimit createMoneyAmountLimit(LocalDateTime start, ApplicationPeriod applicationPeriod, MoneyAmount amount) {
    return new MoneyAmountLimit(start, calculateEnd(start, applicationPeriod), amount);
  }

  private LocalDateTime calculateEnd(LocalDateTime start, ApplicationPeriod period) {
    return start.plus(period.getDuration());
  }
}

package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.time.LocalDateTime;

public class LimitFactory {
  private final LocalDateTime start;

  public LimitFactory(Clock clock) {
    start = clock.getCurrentTime();
  }

  public StockQuantityLimit createStockQuantityLimit(ApplicationPeriod applicationPeriod, int stockQuantity) {

    return new StockQuantityLimit(start, calculateEnd(start, applicationPeriod), stockQuantity);
  }

  public MoneyAmountLimit createMoneyAmountLimit(ApplicationPeriod applicationPeriod, MoneyAmount amount) {
    return new MoneyAmountLimit(start, calculateEnd(start, applicationPeriod), amount);
  }

  private LocalDateTime calculateEnd(LocalDateTime start, ApplicationPeriod period) {
    return start.plus(period.getDuration());
  }
}

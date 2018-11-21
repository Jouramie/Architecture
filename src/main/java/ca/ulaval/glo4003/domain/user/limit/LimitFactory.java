package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.time.LocalDateTime;

public class LimitFactory {
  private final Clock clock;

  public LimitFactory(Clock clock) {
    this.clock = clock;
  }

  public StockQuantityLimit createStockQuantityLimit(ApplicationPeriod applicationPeriod, int stockQuantity) {

    return new StockQuantityLimit(clock.getCurrentTime(), calculateEnd(clock.getCurrentTime(), applicationPeriod), stockQuantity);
  }

  public MoneyAmountLimit createMoneyAmountLimit(ApplicationPeriod applicationPeriod, MoneyAmount amount) {
    return new MoneyAmountLimit(clock.getCurrentTime(), calculateEnd(clock.getCurrentTime(), applicationPeriod), amount);
  }

  private LocalDateTime calculateEnd(LocalDateTime start, ApplicationPeriod period) {
    return start.plus(period.getDuration());
  }
}

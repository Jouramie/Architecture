package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import java.time.LocalDateTime;
import javax.inject.Inject;

@Component
public class LimitFactory {
  private final Clock clock;

  @Inject
  public LimitFactory(Clock clock) {
    this.clock = clock;
  }

  public StockQuantityLimit createStockQuantityLimit(ApplicationPeriod applicationPeriod, int stockQuantity) {
    LocalDateTime beginDate = clock.getCurrentTime();
    return new StockQuantityLimit(beginDate, calculateEnd(beginDate, applicationPeriod), stockQuantity);
  }

  public MoneyAmountLimit createMoneyAmountLimit(ApplicationPeriod applicationPeriod, MoneyAmount moneyAmount) {
    LocalDateTime beginDate = clock.getCurrentTime();
    return new MoneyAmountLimit(beginDate, calculateEnd(beginDate, applicationPeriod), moneyAmount);
  }

  public NullLimit createNullLimit() {
    return new NullLimit();
  }

  private LocalDateTime calculateEnd(LocalDateTime beginDate, ApplicationPeriod period) {
    return beginDate.plus(period.getDuration());
  }
}

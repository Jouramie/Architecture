package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import org.joda.time.DateTime;

public class LimitFactory {
  public Limit create(DateTime start, ApplicationPeriod applicationPeriod, int stockQuantity) {
    return new StockQuantityLimit(start, calculateEnd(start, applicationPeriod), stockQuantity);
  }

  public Limit create(DateTime start, ApplicationPeriod applicationPeriod, MoneyAmount amount) {
    return new MoneyAmountLimit(start, calculateEnd(start, applicationPeriod), amount);
  }

  private DateTime calculateEnd(DateTime start, ApplicationPeriod period) {
    return start.plus(period.getDuration().toMillis());
  }
}

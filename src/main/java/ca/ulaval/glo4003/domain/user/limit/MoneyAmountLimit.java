package ca.ulaval.glo4003.domain.user.limit;

import ca.ulaval.glo4003.domain.money.MoneyAmount;
import org.joda.time.DateTime;

public class MoneyAmountLimit extends Limit {
  private final MoneyAmount amount;

  public MoneyAmountLimit(DateTime start, DateTime end, MoneyAmount amount) {
    super(start, end);
    this.amount = amount;
  }
}

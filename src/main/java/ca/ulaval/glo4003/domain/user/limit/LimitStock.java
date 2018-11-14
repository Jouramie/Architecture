package ca.ulaval.glo4003.domain.user.limit;

import org.joda.time.DateTime;

public class LimitStock extends Limit {
  private final int numberOfStock;

  LimitStock(ApplicationPeriod period, DateTime start, int numberOfStock) {
    super(period, start);
    this.numberOfStock = numberOfStock;
  }
}

package ca.ulaval.glo4003.domain.user.limit;

import org.joda.time.DateTime;

public abstract class Limit {
  private final ApplicationPeriod period;
  private final DateTime start;
  private final DateTime end;

  Limit(ApplicationPeriod period, DateTime start) {
    this.period = period;
    this.start = start;
    end = calculateEnd();
  }

  private DateTime calculateEnd() {
    return start.plus(period.getDuration().toMillis());
  }
}

package ca.ulaval.glo4003.domain.user.limit;

import org.joda.time.DateTime;

public abstract class Limit {
  public final DateTime start;
  public final DateTime end;

  public Limit(DateTime start, DateTime end) {
    this.start = start;
    this.end = end;
  }
}

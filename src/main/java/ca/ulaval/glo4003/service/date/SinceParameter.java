package ca.ulaval.glo4003.service.date;

import java.time.Duration;

public enum SinceParameter {
  LAST_FIVE_DAYS(Duration.ofDays(5)),
  LAST_THIRTY_DAYS(Duration.ofDays(30)),
  LAST_YEAR(Duration.ofDays(365));

  private final Duration since;

  SinceParameter(Duration since) {
    this.since = since;
  }

  public long toDays() {
    return since.toDays();
  }
}

package ca.ulaval.glo4003.domain.user.limit;

import java.time.LocalDateTime;

public abstract class Limit {
  public final LocalDateTime start;
  public final LocalDateTime end;

  public Limit(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
  }
}

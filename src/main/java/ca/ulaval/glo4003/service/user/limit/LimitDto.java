package ca.ulaval.glo4003.service.user.limit;

import java.time.LocalDateTime;

public abstract class LimitDto {

  public final LocalDateTime from;
  public final LocalDateTime to;

  public LimitDto(LocalDateTime from, LocalDateTime to) {
    this.from = from;
    this.to = to;
  }
}

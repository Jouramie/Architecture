package ca.ulaval.glo4003.service.user.limit;

import java.time.LocalDateTime;

public abstract class LimitDto {

  public final LocalDateTime begin;
  public final LocalDateTime end;

  public LimitDto(LocalDateTime begin, LocalDateTime end) {
    this.begin = begin;
    this.end = end;
  }
}

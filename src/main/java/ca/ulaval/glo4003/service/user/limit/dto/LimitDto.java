package ca.ulaval.glo4003.service.user.limit.dto;

import java.time.LocalDateTime;

public abstract class LimitDto {

  public final LocalDateTime beginDate;
  public final LocalDateTime endDate;

  public LimitDto(LocalDateTime beginDate, LocalDateTime endDate) {
    this.beginDate = beginDate;
    this.endDate = endDate;
  }
}

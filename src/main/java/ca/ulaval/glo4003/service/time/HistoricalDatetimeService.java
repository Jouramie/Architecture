package ca.ulaval.glo4003.service.time;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.infrastructure.injection.Component;
import java.time.LocalDateTime;
import javax.inject.Inject;

@Component
public class HistoricalDatetimeService {

  private final Clock clock;

  @Inject
  public HistoricalDatetimeService(Clock clock) {
    this.clock = clock;
  }

  public LocalDateTime getFiveDaysAgo() {
    return clock.getCurrentTime().minusDays(5);
  }

  public LocalDateTime getThirtyDaysAgo() {
    return clock.getCurrentTime().minusDays(30);
  }

  public LocalDateTime getOneYearAgo() {
    return clock.getCurrentTime().minusYears(1);
  }
}

package ca.ulaval.glo4003.service.time;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.service.Component;
import java.time.LocalDate;
import javax.inject.Inject;

@Component
public class HistoricalDateService {

  private final Clock clock;

  @Inject
  public HistoricalDateService(Clock clock) {
    this.clock = clock;
  }

  public LocalDate getFiveDaysAgo() {
    return clock.getCurrentTime().minusDays(5).toLocalDate();
  }

  public LocalDate getStartOfCurrentMonth() {
    return clock.getCurrentTime().withDayOfMonth(1).toLocalDate();
  }

  public LocalDate getThirtyDaysAgo() {
    return clock.getCurrentTime().minusDays(30).toLocalDate();
  }

  public LocalDate getOneYearAgo() {
    return clock.getCurrentTime().minusYears(1).toLocalDate();
  }

  public LocalDate getFiveYearsAgo() {
    return clock.getCurrentTime().minusYears(5).toLocalDate();
  }

  public LocalDate getTenYearsAgo() {
    return clock.getCurrentTime().minusYears(10).toLocalDate();
  }
}

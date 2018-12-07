package ca.ulaval.glo4003.service.date;

import ca.ulaval.glo4003.domain.Component;
import ca.ulaval.glo4003.domain.clock.ReadableClock;
import java.time.LocalDate;
import javax.inject.Inject;

@Component
public class DateService {

  private final ReadableClock clock;

  @Inject
  public DateService(ReadableClock clock) {
    this.clock = clock;
  }

  public LocalDate getFiveDaysAgo() {
    return clock.getCurrentDate().minusDays(5);
  }

  public LocalDate getStartOfCurrentMonth() {
    return clock.getCurrentDate().withDayOfMonth(1);
  }

  public LocalDate getThirtyDaysAgo() {
    return clock.getCurrentDate().minusDays(30);
  }

  public LocalDate getOneYearAgo() {
    return clock.getCurrentDate().minusYears(1);
  }

  public LocalDate getFiveYearsAgo() {
    return clock.getCurrentDate().minusYears(5);
  }

  public LocalDate getTenYearsAgo() {
    return clock.getCurrentDate().minusYears(10);
  }

  public LocalDate getDateSince(SinceParameter since) {
    return clock.getCurrentDate().minusDays(since.toDays());
  }
}

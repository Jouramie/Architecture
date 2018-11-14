package ca.ulaval.glo4003.domain.user.limit;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Test;

public class LimitFactoryTest {

  private final DateTime start = new DateTime();
  private final LimitFactory limitFactory = new LimitFactory();

  @Test
  public void whenCreatingLimit_thenStartDateIsTheGivenOne() {
    Limit result = limitFactory.create(start, ApplicationPeriod.WEEKLY, 0);

    assertThat(result.start).isEqualTo(start);
  }

  @Test
  public void givenDailyLimit_whenCreatingLimit_thenEndIsOneDayLater() {
    Limit result = limitFactory.create(start, ApplicationPeriod.DAILY, 0);

    int resultDuration = Days.daysBetween(result.start.toLocalDate(), result.end.toLocalDate()).getDays();
    assertThat(resultDuration).isEqualTo(1);
  }

  @Test
  public void givenWeeklyLimit_whenCreatingLimit_thenEndIsSevenDaysLater() {
    Limit result = limitFactory.create(start, ApplicationPeriod.WEEKLY, 0);

    int resultDuration = Days.daysBetween(result.start.toLocalDate(), result.end.toLocalDate()).getDays();
    assertThat(resultDuration).isEqualTo(7);
  }

  @Test
  public void givenMonthlyLimit_whenCreatingLimit_thenEndIsThirtyDaysLater() {
    Limit result = limitFactory.create(start, ApplicationPeriod.MONTHLY, 0);

    int resultDuration = Days.daysBetween(result.start.toLocalDate(), result.end.toLocalDate()).getDays();
    assertThat(resultDuration).isEqualTo(30);
  }
}

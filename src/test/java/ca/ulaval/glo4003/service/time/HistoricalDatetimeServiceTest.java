package ca.ulaval.glo4003.service.time;

import static org.junit.Assert.assertEquals;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.Before;
import org.junit.Test;

public class HistoricalDatetimeServiceTest {

  private static final LocalDateTime START_TIME = LocalDateTime.of(1984, Month.JANUARY, 24, 10, 0);

  private Clock clock;
  private HistoricalDatetimeService datetimeService;

  @Before
  public void initialize() {
    clock = new Clock(START_TIME, Duration.ZERO);
    datetimeService = new HistoricalDatetimeService(clock);
  }

  @Test
  public void whenGettingDateFiveDaysAgo_thenReturnAFiveDayEarlierDateAtTheSameHour() {
    LocalDateTime expected = START_TIME.minusDays(5);

    LocalDateTime fiveDaysAgo = datetimeService.getFiveDaysAgo();

    assertEquals(expected, fiveDaysAgo);
  }

  @Test
  public void whenGettingDateThirtyDaysAgo_thenReturnAThirtyDayEarlierDateAtTheSameHourOfTheDay() {
    LocalDateTime expected = START_TIME.minusDays(30);

    LocalDateTime thirtyDaysAgo = datetimeService.getThirtyDaysAgo();

    assertEquals(expected, thirtyDaysAgo);
  }

  @Test
  public void whenGettingDateThirtyDaysAgo_thenReturnAYearEarlierDateAtTheSameHourOfTheDay() {
    LocalDateTime expected = START_TIME.minusYears(1);

    LocalDateTime oneYearAgo = datetimeService.getOneYearAgo();

    assertEquals(expected, oneYearAgo);
  }
}

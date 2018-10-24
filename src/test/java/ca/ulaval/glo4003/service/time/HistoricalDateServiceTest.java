package ca.ulaval.glo4003.service.time;

import static org.junit.Assert.assertEquals;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.Before;
import org.junit.Test;

public class HistoricalDateServiceTest {

  private static final LocalDateTime START_TIME = LocalDateTime.of(1984, Month.JANUARY, 24, 10, 0);

  private Clock clock;
  private HistoricalDateService dateService;

  @Before
  public void initialize() {
    clock = new Clock(START_TIME, Duration.ZERO);
    dateService = new HistoricalDateService(clock);
  }

  @Test
  public void whenGettingDateFiveDaysAgo_thenReturnAFiveDayEarlierDate() {
    LocalDate expected = START_TIME.minusDays(5).toLocalDate();

    LocalDate fiveDaysAgo = dateService.getFiveDaysAgo();

    assertEquals(expected, fiveDaysAgo);
  }

  @Test
  public void whenGettingDateStartOfCurrentMonth_thenReturnFirstDayOfTheMonth() {
    LocalDate expected = START_TIME.withDayOfMonth(1).toLocalDate();

    LocalDate startOfCurrentMonth = dateService.getStartOfCurrentMonth();

    assertEquals(expected, startOfCurrentMonth);
  }

  @Test
  public void whenGettingDateThirtyDaysAgo_thenReturnAThirtyDayEarlierDate() {
    LocalDate expected = START_TIME.minusDays(30).toLocalDate();

    LocalDate thirtyDaysAgo = dateService.getThirtyDaysAgo();

    assertEquals(expected, thirtyDaysAgo);
  }

  @Test
  public void whenGettingDateOneYearAgo_thenReturnAYearEarlierDate() {
    LocalDate expected = START_TIME.minusYears(1).toLocalDate();

    LocalDate oneYearAgo = dateService.getOneYearAgo();

    assertEquals(expected, oneYearAgo);
  }

  @Test
  public void whenGettingDateFiveYearsAgo_thenReturnFiveYearsEarlierDate() {
    LocalDate expected = START_TIME.minusYears(5).toLocalDate();

    LocalDate fiveYearsAgo = dateService.getFiveYearsAgo();

    assertEquals(expected, fiveYearsAgo);
  }

  @Test
  public void whenGettingDateTenYearsAgo_thenReturnTenYearsEarlierDate() {
    LocalDate expected = START_TIME.minusYears(10).toLocalDate();

    LocalDate tenYearsAgo = dateService.getTenYearsAgo();

    assertEquals(expected, tenYearsAgo);
  }
}

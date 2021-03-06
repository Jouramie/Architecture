package ca.ulaval.glo4003.service.date;

import static org.junit.Assert.assertEquals;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import org.junit.Before;
import org.junit.Test;

public class DateServiceTest {

  private static final LocalDate SOME_START_DATE = LocalDate.of(1984, Month.JANUARY, 24);

  private Clock clock;
  private DateService dateService;

  @Before
  public void initialize() {
    clock = new Clock(SOME_START_DATE.atTime(0, 0, 0), Duration.ZERO);
    dateService = new DateService(clock);
  }

  @Test
  public void whenGetCurrentDate_thenCurrentDateIsGottenFromTheClock() {
    LocalDate expected = dateService.getCurrentDate();

    assertEquals(expected, SOME_START_DATE);
  }

  @Test
  public void whenGettingDateFiveDaysAgo_thenReturnAFiveDayEarlierDate() {
    LocalDate expected = SOME_START_DATE.minusDays(5);

    LocalDate fiveDaysAgo = dateService.getFiveDaysAgo();

    assertEquals(expected, fiveDaysAgo);
  }

  @Test
  public void whenGettingDateStartOfCurrentMonth_thenReturnFirstDayOfTheMonth() {
    LocalDate expected = SOME_START_DATE.withDayOfMonth(1);

    LocalDate startOfCurrentMonth = dateService.getStartOfCurrentMonth();

    assertEquals(expected, startOfCurrentMonth);
  }

  @Test
  public void whenGettingDateThirtyDaysAgo_thenReturnAThirtyDayEarlierDate() {
    LocalDate expected = SOME_START_DATE.minusDays(30);

    LocalDate thirtyDaysAgo = dateService.getThirtyDaysAgo();

    assertEquals(expected, thirtyDaysAgo);
  }

  @Test
  public void whenGettingDateOneYearAgo_thenReturnAYearEarlierDate() {
    LocalDate expected = SOME_START_DATE.minusYears(1);

    LocalDate oneYearAgo = dateService.getOneYearAgo();

    assertEquals(expected, oneYearAgo);
  }

  @Test
  public void whenGettingDateFiveYearsAgo_thenReturnFiveYearsEarlierDate() {
    LocalDate expected = SOME_START_DATE.minusYears(5);

    LocalDate fiveYearsAgo = dateService.getFiveYearsAgo();

    assertEquals(expected, fiveYearsAgo);
  }

  @Test
  public void whenGettingDateTenYearsAgo_thenReturnTenYearsEarlierDate() {
    LocalDate expected = SOME_START_DATE.minusYears(10);

    LocalDate tenYearsAgo = dateService.getTenYearsAgo();

    assertEquals(expected, tenYearsAgo);
  }

  @Test
  public void whenGettingDateSinceFiveDays_thenReturnADurationEarlierDate() {
    LocalDate expected = SOME_START_DATE.minusDays(5);
    SinceParameter sinceFiveDays = SinceParameter.LAST_FIVE_DAYS;

    LocalDate dateSince = dateService.getDateSince(sinceFiveDays);

    assertEquals(expected, dateSince);
  }

  @Test
  public void whenGettingDateSinceThirtyDays_thenReturnADurationEarlierDate() {
    LocalDate expected = SOME_START_DATE.minusDays(30);
    SinceParameter sinceThirtyDays = SinceParameter.LAST_THIRTY_DAYS;

    LocalDate dateSince = dateService.getDateSince(sinceThirtyDays);

    assertEquals(expected, dateSince);
  }

  @Test
  public void whenGettingDateSinceOneYear_thenReturnADurationEarlierDate() {
    LocalDate expected = SOME_START_DATE.minusYears(1);
    SinceParameter sinceOneYear = SinceParameter.LAST_YEAR;

    LocalDate dateSince = dateService.getDateSince(sinceOneYear);

    assertEquals(expected, dateSince);
  }
}

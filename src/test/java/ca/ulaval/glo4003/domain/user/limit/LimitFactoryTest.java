package ca.ulaval.glo4003.domain.user.limit;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.Test;

public class LimitFactoryTest {

  private final LocalDateTime start = LocalDateTime.of(2018, 11, 16, 12, 4);
  private final LimitFactory limitFactory = new LimitFactory();

  @Test
  public void whenCreatingLimit_thenStartDateIsTheGivenOne() {
    TemporaryLimit result = limitFactory.createStockQuantityLimit(start, ApplicationPeriod.WEEKLY, 0);

    assertThat(result.start).isEqualTo(start);
  }

  @Test
  public void givenDailyLimit_whenCreatingLimit_thenEndIs24HoursLater() {
    TemporaryLimit result = limitFactory.createStockQuantityLimit(start, ApplicationPeriod.DAILY, 0);

    Duration resultDuration = Duration.between(result.start, result.end);
    assertThat(resultDuration.toHours()).isEqualTo(24);
  }

  @Test
  public void givenWeeklyLimit_whenCreatingLimit_thenEndIsSevenDaysLater() {
    TemporaryLimit result = limitFactory.createStockQuantityLimit(start, ApplicationPeriod.WEEKLY, 0);

    Duration resultDuration = Duration.between(result.start, result.end);
    assertThat(resultDuration.toDays()).isEqualTo(7);
  }

  @Test
  public void givenMonthlyLimit_whenCreatingLimit_thenEndIsThirtyDaysLater() {
    TemporaryLimit result = limitFactory.createStockQuantityLimit(start, ApplicationPeriod.MONTHLY, 0);

    Duration resultDuration = Duration.between(result.start, result.end);
    assertThat(resultDuration.toDays()).isEqualTo(30);
  }
}

package ca.ulaval.glo4003.domain.user.limit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class LimitFactoryTest {

  private final LocalDateTime start = LocalDateTime.of(2018, 11, 16, 12, 4);

  @Mock
  Clock clock;

  private LimitFactory limitFactory;

  @Before
  public void setUp() {
    limitFactory = new LimitFactory(clock);
    given(clock.getCurrentTime()).willReturn(start);
  }

  @Test
  public void whenCreatingLimit_thenStartDateIsTheGivenOne() {
    Limit result = limitFactory.createStockQuantityLimit(ApplicationPeriod.WEEKLY, 0);

    assertThat(result.start).isEqualTo(start);
  }

  @Test
  public void givenDailyLimit_whenCreatingLimit_thenEndIs24HoursLater() {
    Limit result = limitFactory.createStockQuantityLimit(ApplicationPeriod.DAILY, 0);

    Duration resultDuration = Duration.between(result.start, result.end);
    assertThat(resultDuration.toHours()).isEqualTo(24);
  }

  @Test
  public void givenWeeklyLimit_whenCreatingLimit_thenEndIsSevenDaysLater() {
    Limit result = limitFactory.createStockQuantityLimit(ApplicationPeriod.WEEKLY, 0);

    Duration resultDuration = Duration.between(result.start, result.end);
    assertThat(resultDuration.toDays()).isEqualTo(7);
  }

  @Test
  public void givenMonthlyLimit_whenCreatingLimit_thenEndIsThirtyDaysLater() {
    Limit result = limitFactory.createStockQuantityLimit(ApplicationPeriod.MONTHLY, 0);

    Duration resultDuration = Duration.between(result.start, result.end);
    assertThat(resultDuration.toDays()).isEqualTo(30);
  }
}

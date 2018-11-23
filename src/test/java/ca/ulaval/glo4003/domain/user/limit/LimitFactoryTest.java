package ca.ulaval.glo4003.domain.user.limit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LimitFactoryTest {

  private final LocalDateTime start = LocalDateTime.of(2018, 11, 16, 12, 4);

  @Mock
  Clock clock;

  private LimitFactory limitFactory;

  @Before
  public void setUp() {
    given(clock.getCurrentTime()).willReturn(start);
    limitFactory = new LimitFactory(clock);
  }

  @Test
  public void whenCreatingLimit_thenStartDateIsTheGivenOne() {
    TemporaryLimit result = limitFactory.createStockQuantityLimit(ApplicationPeriod.WEEKLY, 0);

    assertThat(result.begin).isEqualTo(start);
  }

  @Test
  public void givenDailyLimit_whenCreatingLimit_thenEndIs24HoursLater() {
    TemporaryLimit result = limitFactory.createStockQuantityLimit(ApplicationPeriod.DAILY, 0);

    Duration resultDuration = Duration.between(result.begin, result.end);
    assertThat(resultDuration.toHours()).isEqualTo(24);
  }

  @Test
  public void givenWeeklyLimit_whenCreatingLimit_thenEndIsSevenDaysLater() {
    TemporaryLimit result = limitFactory.createStockQuantityLimit(ApplicationPeriod.WEEKLY, 0);

    Duration resultDuration = Duration.between(result.begin, result.end);
    assertThat(resultDuration.toDays()).isEqualTo(7);
  }

  @Test
  public void givenMonthlyLimit_whenCreatingLimit_thenEndIsThirtyDaysLater() {
    TemporaryLimit result = limitFactory.createStockQuantityLimit(ApplicationPeriod.MONTHLY, 0);

    Duration resultDuration = Duration.between(result.begin, result.end);
    assertThat(resultDuration.toDays()).isEqualTo(30);
  }
}

package ca.ulaval.glo4003.domain.clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

public class ClockTest {
  private final LocalDateTime SOME_START_TIME = LocalDateTime.of(2018, 9, 22, 8, 0, 0);
  private final LocalDateTime SOME_NEW_TIME = LocalDateTime.of(2018, 11, 8, 12, 0, 0);
  private final Duration SOME_TICK_STEP = Duration.ofMinutes(30);

  private Clock clock;

  @Before
  public void setupClock() {
    clock = new Clock(SOME_START_TIME, SOME_TICK_STEP);
  }

  @Test
  public void givenConfiguredClock_whenGetCurrentTime_thenReturnStartTime() {
    LocalDateTime currentTime = clock.getCurrentTime();

    assertThat(currentTime).isEqualTo(SOME_START_TIME);
  }
  
  @Test
  public void whenTick_thenTimeIsAdvancedByConfiguredTickStep() {
    clock.tick();

    assertThat(clock.getCurrentTime()).isEqualTo(SOME_START_TIME.plus(SOME_TICK_STEP));
  }

  @Test
  public void givenClockWithObservingObject_whenTick_thenObservingObjectIsCalledWithCurrentTime() {
    ClockObserver clockObserver = mock(ClockObserver.class);
    clock.register(clockObserver);

    clock.tick();

    verify(clockObserver).onTick(clock.getCurrentTime());
  }
}

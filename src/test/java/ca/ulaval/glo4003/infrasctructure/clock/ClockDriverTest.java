package ca.ulaval.glo4003.infrasctructure.clock;

import static java.lang.Thread.sleep;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClockDriverTest {
  private final Duration STOCK_UPDATE_FREQ = Duration.ofMillis(100);
  @Mock
  private Clock clock;
  private ClockDriver driver;

  @Before
  public void setupClockDriver() {
    driver = new ClockDriver(clock, STOCK_UPDATE_FREQ);
  }

  @Test
  public void whenUpdaterIsStarted_thenLiveStockSimulatorIsCalledAtSpecfiedFrequency() throws InterruptedException {
    driver.start();
    sleep(3 * STOCK_UPDATE_FREQ.toMillis());
    driver.stop();

    verify(clock, atLeast(3)).tick();
    verify(clock, atMost(4)).tick();
  }

  @Test
  public void whenUpdaterIsStopped_thenLiveStockSimulatorIsNotCalledAnymore() throws InterruptedException {
    driver.start();
    driver.stop();
    sleep(3 * STOCK_UPDATE_FREQ.toMillis());

    verify(clock, atMost(1)).tick();
  }
}

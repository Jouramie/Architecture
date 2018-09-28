package ca.ulaval.glo4003.infrastructure.clock;

import static java.lang.Thread.sleep;

import ca.ulaval.glo4003.domain.clock.Clock;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClockDriver {
  private final Clock clock;
  private final Duration updateFreqMs;
  private final AtomicBoolean isRunning;
  private Thread thread;

  ClockDriver(Clock clock, Duration updateFreqMs) {
    this.clock = clock;
    this.updateFreqMs = updateFreqMs;
    isRunning = new AtomicBoolean(false);
  }

  public void start() {
    if (!isRunning.get()) {
      isRunning.set(true);
      thread = new Thread(this::run);
      thread.start();
    }
  }

  public void stop() {
    try {
      if (isRunning.get()) {
        isRunning.set(false);
        thread.interrupt();
        thread.join();
      }
    } catch (InterruptedException e) {
      // Catch all interruptions
    }
  }

  private void run() {
    try {
      while (isRunning.get()) {
        clock.tick();
        sleep(updateFreqMs.toMillis());
      }
    } catch (InterruptedException e) {
      // Catch all interruptions
    }
  }
}

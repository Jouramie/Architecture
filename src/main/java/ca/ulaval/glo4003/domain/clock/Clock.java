package ca.ulaval.glo4003.domain.clock;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Clock extends ClockObservable implements ReadableClock {
  private final Duration tickStep;
  private LocalDateTime currentTime;

  public Clock(LocalDateTime startTime, Duration tickStep) {
    currentTime = startTime;
    this.tickStep = tickStep;
  }

  @Override
  public LocalDateTime getCurrentTime() {
    return currentTime;
  }

  @Override
  public LocalDate getCurrentDate() {
    return currentTime.toLocalDate();
  }

  public void tick() {
    currentTime = currentTime.plus(tickStep);
    notifyObservers(currentTime);
  }
}

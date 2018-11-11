package ca.ulaval.glo4003.domain.clock;

import java.time.Duration;
import java.time.LocalDateTime;

public class Clock extends ClockObservable {
  private final Duration tickStep;
  private LocalDateTime currentTime;

  public Clock(LocalDateTime startTime, Duration tickStep) {
    currentTime = startTime;
    this.tickStep = tickStep;
  }

  public LocalDateTime getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(LocalDateTime newCurrentTime) {
    this.currentTime = newCurrentTime;
  }

  public void tick() {
    currentTime = currentTime.plus(tickStep);
    notifyObservers(currentTime);
  }
}

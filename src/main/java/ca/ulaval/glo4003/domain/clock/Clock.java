package ca.ulaval.glo4003.domain.clock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Clock {
  private final Duration tickStep;
  private final List<ClockObserver> observers;
  private LocalDateTime currentTime;

  public Clock(LocalDateTime startTime, Duration tickStep) {
    currentTime = startTime;
    this.tickStep = tickStep;
    observers = new ArrayList<>();
  }

  public LocalDateTime getCurrentTime() {
    return currentTime;
  }

  public void tick() {
    currentTime = currentTime.plus(tickStep);

    observers.forEach(observer -> observer.onTick(currentTime));
  }

  public void register(ClockObserver observer) {
    observers.add(observer);
  }
}

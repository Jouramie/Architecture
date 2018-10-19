package ca.ulaval.glo4003.domain.clock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class ClockObservable {

  private final List<ClockObserver> observers;

  public ClockObservable() {
    observers = new ArrayList<>();
  }

  public void register(ClockObserver observer) {
    observers.add(observer);
  }

  protected void notifyObservers(LocalDateTime currentTime) {
    observers.forEach(observer -> observer.onTick(currentTime));
  }
}

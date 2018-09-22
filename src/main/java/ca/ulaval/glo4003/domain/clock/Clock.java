package ca.ulaval.glo4003.domain.clock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Clock {
    private LocalDateTime currentTime;
    private final Duration tickStep;
    List<ClockObserver> observers;

    public Clock(LocalDateTime startTime, Duration tickStep) {
        this.currentTime = startTime;
        this.tickStep = tickStep;
        this.observers = new ArrayList<>();
    }

    public LocalDateTime getCurrentTime() {
        return this.currentTime;
    }

    public void tick() {
        this.currentTime = this.currentTime.plus(this.tickStep);

        observers.stream().forEach((observer -> observer.onTick(this.currentTime)));
    }

    public void register(ClockObserver observer) {
        this.observers.add(observer);
    }
}

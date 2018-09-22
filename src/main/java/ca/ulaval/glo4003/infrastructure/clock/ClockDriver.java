package ca.ulaval.glo4003.infrastructure.clock;

import ca.ulaval.glo4003.domain.clock.Clock;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class ClockDriver {
    private final Clock clock;
    private final Duration updateFreqMs;
    private Thread thread;
    private AtomicBoolean isRunning;

    public ClockDriver(Clock clock, Duration updateFreqMs) {
        this.clock = clock;
        this.updateFreqMs = updateFreqMs;
        this.isRunning = new AtomicBoolean(false);
    }

    public void start() {
        if(!isRunning.get()) {
            this.isRunning.set(true);
            thread = new Thread(this::run);
            thread.start();
        }
    }

    public void stop() {
        try {
            if(isRunning.get()) {
                this.isRunning.set(false);
                this.thread.interrupt();
                this.thread.join();
            }
        } catch (InterruptedException e) {}
    }

    private void run() {
        try {
            while(isRunning.get()) {
                this.clock.tick();
                sleep(this.updateFreqMs.toMillis());
            }
        } catch (InterruptedException e) {}
    }
}

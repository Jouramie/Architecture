package ca.ulaval.glo4003.infrastructure.simulator;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class LiveStockUpdater {
    private final LiveStockSimulator simulator;
    private final long updateFreqMs;
    private Thread thread;
    private AtomicBoolean isRunning;

    public LiveStockUpdater(LiveStockSimulator simulator, long updateFreqMs) {
        this.simulator = simulator;
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
                this.simulator.update();
                sleep(this.updateFreqMs);
            }
        } catch (InterruptedException e) {}
    }
}

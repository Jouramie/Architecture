package ca.ulaval.glo4003.infrastructure.simulator;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.stock.StockRepositoryInMemory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.lang.Thread.sleep;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;

@RunWith(MockitoJUnitRunner.class)
public class LiveStockUpdaterTest {
    private final long STOCK_UPDATE_FREQ = 100;
    @Mock
    private LiveStockSimulator simulator;
    private LiveStockUpdater updater;

    @Before
    public void setupLiveStockUpdater() {
        updater = new LiveStockUpdater(simulator, STOCK_UPDATE_FREQ);
    }

    @Test
    public void whenUpdaterIsStarted_thenLiveStockSimulatorIsCalledAtSpecfiedFrequency() throws InterruptedException {
        updater.start();
        sleep(3 * STOCK_UPDATE_FREQ);
        updater.stop();

        verify(simulator, atLeast(3)).update();
        verify(simulator, atMost(4)).update();
    }

    @Test
    public void whenUpdaterIsStopped_thenLiveStockSimulatorIsNotCalledAnymore() throws InterruptedException {
        updater.start();
        updater.stop();
        sleep(3 * STOCK_UPDATE_FREQ);

        verify(simulator, atMost(1)).update();
    }
}

package ca.ulaval.glo4003.infrastructure.simulator;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.infrastructure.simulator.LiveStockSimulator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Matchers.anyDouble;

@RunWith(MockitoJUnitRunner.class)
public class LiveStockSimulatorTest {
    @Mock
    private Stock someStock;
    @Mock
    private Stock someOtherStock;
    @Mock
    private StockRepository repository;

    private LiveStockSimulator simulator;

    @Before
    public void setupLiveStockSimulator() {
        given(repository.getAll()).willReturn(Arrays.asList(someStock, someOtherStock));
        simulator = new LiveStockSimulator(repository);
    }

    @Test
    public void whenUpdate_thenEachStockValueIsUpdated() {
        simulator.update();

        verify(someStock).updateValue(anyDouble());
        verify(someOtherStock).updateValue(anyDouble());
    }
}

package ca.ulaval.glo4003.infrastructure.stock;

import static org.mockito.BDDMockito.verify;
import static org.mockito.Matchers.anyDouble;

import ca.ulaval.glo4003.domain.stock.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimulatedStockValueRetrieverTest {
  @Mock
  private Stock someStock;

  private SimulatedStockValueRetriever simulator;

  @Before
  public void setupSimulatedStockValueRetriever() {
    simulator = new SimulatedStockValueRetriever();
  }

  @Test
  public void whenUpdateStockValue_thenStockValueIsUpdated() {
    simulator.updateStockValue(someStock);

    verify(someStock).updateValue(anyDouble());
  }
}

package ca.ulaval.glo4003.infrastructure.stock;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Matchers.eq;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.investul.live_stock_emulator.StockSimulator;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimulatedStockValueRetrieverTest {

  private static final double DEFAULT_VARIATION = 0.0;
  private static final double SOME_VARIATION = 0.2;

  @Mock
  private Stock someStock;
  @Mock
  private StockSimulator stockSimulator;

  @InjectMocks
  private SimulatedStockValueRetriever simulator;

  @Before
  public void setupStockSimulator() {
    given(stockSimulator.calculateStockVariation(DEFAULT_VARIATION)).willReturn(SOME_VARIATION);
  }

  @Test
  public void whenUpdateStockValue_thenStockValueIsUpdated() {
    simulator.updateStockValue(someStock);

    verify(someStock).updateValue(eq(new BigDecimal(SOME_VARIATION)));
  }

  @Test
  public void whenUpdateStockValue_thenVariationIsCalculatedWithPreviousVariation() {
    simulator.updateStockValue(someStock);

    simulator.updateStockValue(someStock);

    verify(stockSimulator).calculateStockVariation(SOME_VARIATION);
  }
}

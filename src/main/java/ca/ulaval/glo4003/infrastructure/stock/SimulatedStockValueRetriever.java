package ca.ulaval.glo4003.infrastructure.stock;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.investul.live_stock_emulator.StockSimulator;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class SimulatedStockValueRetriever implements StockValueRetriever {
  private final StockSimulator simulator;
  private final Map<String, Double> stockTitleToPreviousVariation;

  @Inject
  public SimulatedStockValueRetriever(StockSimulator simulator) {
    this.simulator = simulator;
    stockTitleToPreviousVariation = new HashMap<>();
  }

  @Override
  public void updateStockValue(Stock stock) {
    double variation = simulator.calculateStockVariation(getPreviousVariation(stock.getTitle()));
    stock.updateValue(new BigDecimal(variation));
    updatePreviousVariation(stock.getTitle(), variation);
  }

  private double getPreviousVariation(String title) {
    return stockTitleToPreviousVariation.getOrDefault(title, 0.0);
  }

  private void updatePreviousVariation(String title, double variation) {
    stockTitleToPreviousVariation.put(title, variation);
  }
}

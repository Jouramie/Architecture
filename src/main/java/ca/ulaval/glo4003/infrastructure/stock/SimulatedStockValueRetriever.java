package ca.ulaval.glo4003.infrastructure.stock;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.investul.live_stock_emulator.StockSimulator;
import java.util.HashMap;
import java.util.Map;

public class SimulatedStockValueRetriever implements StockValueRetriever {
  private final StockSimulator simulator = new StockSimulator();
  private final Map<String, Double> stockTitleToPreviousVariation = new HashMap<>();

  @Override
  public void updateStockValue(Stock stock) {
    double variation = simulator.calculateStockVariation(getPreviousVariation(stock.getTitle()));
    stock.updateValue(variation);
    updatePreviousVariation(stock.getTitle(), variation);
  }

  private double getPreviousVariation(String title) {
    return stockTitleToPreviousVariation.getOrDefault(title, 0.0);
  }

  private void updatePreviousVariation(String title, double variation) {
    stockTitleToPreviousVariation.put(title, variation);
  }
}

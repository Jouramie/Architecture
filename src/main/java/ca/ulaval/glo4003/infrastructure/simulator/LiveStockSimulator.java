package ca.ulaval.glo4003.infrastructure.simulator;

import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.investul.live_stock_emulator.StockSimulator;

import java.util.HashMap;
import java.util.Map;

public class LiveStockSimulator {
    private StockRepository stockRepository;
    private StockSimulator simulator = new StockSimulator();
    private Map<String, Double> stockTitleToPreviousVariation = new HashMap<>();

    public LiveStockSimulator(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void update() {
        this.stockRepository.getAll().stream().forEach((stock) -> {
            double variation = simulator.calculateStockVariation(getPreviousVariation(stock.getTitle()));
            stock.updateValue(variation);
            updatePreviousVariation(stock.getTitle(), variation);
        });
    }

    private double getPreviousVariation(String title) {
        return stockTitleToPreviousVariation.getOrDefault(title, 0.0);
    }

    private void updatePreviousVariation(String title, double variation) {
        stockTitleToPreviousVariation.put(title, variation);
    }
}

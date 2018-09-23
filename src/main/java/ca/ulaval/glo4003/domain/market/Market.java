package ca.ulaval.glo4003.domain.market;

import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import ca.ulaval.glo4003.domain.market.states.CloseMarketState;
import ca.ulaval.glo4003.domain.market.states.HaltMarketState;
import ca.ulaval.glo4003.domain.market.states.OpenMarketState;
import ca.ulaval.glo4003.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Market {
    private final MarketId id;
    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final StockRepository stockRepository;
    private final StockValueRetriever stockValueRetriever;
    private MarketState currentState;

    public Market(MarketId id, LocalTime openingTime, LocalTime closingTime, StockRepository stockRepository, StockValueRetriever stockValueRetriever) {
        this.id = id;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.stockRepository = stockRepository;
        this.stockValueRetriever = stockValueRetriever;
        this.currentState = new CloseMarketState();
    }

    public MarketId getId() {
        return this.id;
    }

    public LocalTime getOpeningTime() {
        return this.openingTime;
    }

    public LocalTime getClosingTime() {
        return this.closingTime;
    }

    public void halt() {
        setState(new HaltMarketState());
    }

    public boolean isOpen() {
        return this.currentState instanceof OpenMarketState;
    }

    public boolean isHalted() {
        return this.currentState instanceof HaltMarketState;
    }

    public void update(LocalDateTime currentTime) {
        this.currentState.update(this, currentTime);
    }

    public void setState(MarketState newState) {
        this.currentState = newState;
    }

    public void updateAllStockValues() {
        this.stockRepository.getStocksOfMarket(this.getId()).stream().forEach(this.stockValueRetriever::updateStockValue);
    }

    public void closeAllStocks() {
        this.stockRepository.getStocksOfMarket(this.getId()).stream().forEach((stock) -> stock.close());
    }
}

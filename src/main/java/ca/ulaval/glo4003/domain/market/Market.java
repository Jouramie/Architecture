package ca.ulaval.glo4003.domain.market;

import ca.ulaval.glo4003.domain.market.states.CloseMarketState;
import ca.ulaval.glo4003.domain.market.states.HaltMarketState;
import ca.ulaval.glo4003.domain.market.states.OpenMarketState;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockRepository;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
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
    currentState = new CloseMarketState();
  }

  public MarketId getId() {
    return id;
  }

  public LocalTime getOpeningTime() {
    return openingTime;
  }

  public LocalTime getClosingTime() {
    return closingTime;
  }

  public void halt() {
    setState(new HaltMarketState());
  }

  public boolean isOpen() {
    return currentState instanceof OpenMarketState;
  }

  boolean isHalted() {
    return currentState instanceof HaltMarketState;
  }

  public void update(LocalDateTime currentTime) {
    currentState.update(this, currentTime);
  }

  public void setState(MarketState newState) {
    currentState = newState;
  }

  public void updateAllStockValues() {
    stockRepository.getStocksOfMarket(getId()).forEach(stockValueRetriever::updateStockValue);
  }

  public void closeAllStocks() {
    stockRepository.getStocksOfMarket(getId()).forEach(Stock::close);
  }
}

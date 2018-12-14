package ca.ulaval.glo4003.domain.market.state;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.market.MarketState;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Market {
  final List<Stock> stocks;
  final LocalTime openingTime;
  final LocalTime closingTime;
  private final MarketId id;
  private final Currency currency;
  private TradingStatus tradingStatus;
  private MarketState currentState;

  public Market(MarketId id, LocalTime openingTime, LocalTime closingTime, Currency currency,
                List<Stock> stocks, MarketState initialState) {
    this.id = id;
    this.openingTime = openingTime;
    this.closingTime = closingTime;
    this.currency = currency;
    this.stocks = stocks;
    currentState = initialState;
    tradingStatus = TradingStatus.trading();
  }

  public MarketId getId() {
    return id;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void addStock(Stock stock) {
    stocks.add(stock);
  }

  public void halt(String message) {
    tradingStatus = TradingStatus.halted(message);
  }

  public void resume() {
    tradingStatus = TradingStatus.trading();
  }

  public String getHaltMessage() {
    return tradingStatus.haltMessage;
  }

  public boolean isHalted() {
    return tradingStatus.isHalted;
  }

  public void update(LocalDateTime currentTime, StockValueRetriever stockValueRetriever) {
    currentState = currentState.update(this, currentTime, stockValueRetriever);
  }

  public boolean containsStock(String title) {
    return stocks.stream().anyMatch(stock -> stock.getTitle().equals(title));
  }
}

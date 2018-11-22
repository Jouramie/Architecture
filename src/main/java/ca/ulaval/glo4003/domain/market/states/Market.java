package ca.ulaval.glo4003.domain.market.states;

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
    tradingStatus = new TradingStatus(false, "");
  }

  public MarketId getId() {
    return id;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void halt(String message) {
    tradingStatus = new TradingStatus(true, message);
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
}

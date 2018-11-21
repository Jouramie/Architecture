package ca.ulaval.glo4003.domain.market.states;

import ca.ulaval.glo4003.domain.market.MarketState;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class OpenMarketState implements MarketState {
  @Override
  public MarketState update(Market market, LocalDateTime currentTime, StockValueRetriever stockValueRetriever) {
    market.stocks.forEach(stockValueRetriever::updateStockValue);
    LocalTime time = currentTime.toLocalTime();
    if (shouldClose(market, time)) {
      market.stocks.forEach(Stock::saveClosingPrice);
      return new ClosedMarketState();
    }
    return this;
  }

  private boolean shouldClose(Market market, LocalTime time) {
    return time.equals(market.closingTime) || time.isAfter(market.closingTime);
  }
}

package ca.ulaval.glo4003.domain.market.state;

import ca.ulaval.glo4003.domain.market.MarketState;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ClosedMarketState implements MarketState {
  @Override
  public MarketState update(Market market, LocalDateTime currentTime, StockValueRetriever stockValueRetriever) {
    LocalTime time = currentTime.toLocalTime();
    if (shouldOpen(market, time)) {
      market.stocks.forEach(Stock::open);
      return new OpenMarketState();
    }
    return this;
  }

  private boolean shouldOpen(Market market, LocalTime time) {
    return (time.equals(market.openingTime) || time.isAfter(market.openingTime))
        && time.isBefore(market.closingTime);
  }
}

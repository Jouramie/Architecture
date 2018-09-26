package ca.ulaval.glo4003.domain.market.states;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketState;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class OpenMarketState implements MarketState {
  @Override
  public void update(Market market, LocalDateTime currentTime) {
    market.updateAllStockValues();

    LocalTime time = currentTime.toLocalTime();
    LocalTime closingTime = market.getClosingTime();
    if (time.equals(closingTime) || time.isAfter(closingTime)) {
      market.closeAllStocks();
      market.setState(new CloseMarketState());
    }
  }
}

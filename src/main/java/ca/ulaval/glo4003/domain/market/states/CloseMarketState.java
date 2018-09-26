package ca.ulaval.glo4003.domain.market.states;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketState;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CloseMarketState implements MarketState {
  @Override
  public void update(Market market, LocalDateTime currentTime) {
    LocalTime time = currentTime.toLocalTime();
    LocalTime openingTime = market.getOpeningTime();
    if (time.equals(openingTime) || time.isAfter(openingTime)) {
      market.setState(new OpenMarketState());
      market.updateAllStockValues();
    }
  }
}

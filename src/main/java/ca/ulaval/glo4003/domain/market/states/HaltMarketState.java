package ca.ulaval.glo4003.domain.market.states;

import ca.ulaval.glo4003.domain.market.Market;
import ca.ulaval.glo4003.domain.market.MarketState;
import java.time.LocalDateTime;

public class HaltMarketState implements MarketState {
  @Override
  public void update(Market market, LocalDateTime currentTime) {
    // Market is halted, do nothing
  }
}

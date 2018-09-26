package ca.ulaval.glo4003.domain.market;

import java.time.LocalDateTime;

public interface MarketState {
  void update(Market market, LocalDateTime currentTime);
}

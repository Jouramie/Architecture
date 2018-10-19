package ca.ulaval.glo4003.infrastructure.market;

import ca.ulaval.glo4003.domain.clock.Clock;
import ca.ulaval.glo4003.domain.clock.ClockObserver;
import ca.ulaval.glo4003.domain.market.MarketRepository;
import java.time.LocalDateTime;
import javax.inject.Inject;

public class MarketUpdater implements ClockObserver {
  private final MarketRepository marketRepository;

  @Inject
  public MarketUpdater(Clock clock, MarketRepository marketRepository) {
    clock.register(this);
    this.marketRepository = marketRepository;
  }

  @Override
  public void onTick(LocalDateTime currentTime) {
    marketRepository.findAll().forEach(market -> market.update(currentTime));
  }
}

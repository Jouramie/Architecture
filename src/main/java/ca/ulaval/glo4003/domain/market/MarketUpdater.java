package ca.ulaval.glo4003.domain.market;

import ca.ulaval.glo4003.domain.clock.ClockObservable;
import ca.ulaval.glo4003.domain.clock.ClockObserver;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.time.LocalDateTime;
import javax.inject.Inject;

public class MarketUpdater implements ClockObserver {
  private final MarketRepository marketRepository;
  private final StockValueRetriever stockValueRetriever;

  @Inject
  public MarketUpdater(ClockObservable clockObservable,
                       MarketRepository marketRepository,
                       StockValueRetriever stockValueRetriever) {
    clockObservable.register(this);
    this.stockValueRetriever = stockValueRetriever;
    this.marketRepository = marketRepository;
  }

  @Override
  public void onTick(LocalDateTime currentTime) {
    marketRepository.findAll().forEach(market -> market.update(currentTime, stockValueRetriever));
  }
}

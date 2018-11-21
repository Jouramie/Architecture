package ca.ulaval.glo4003.domain.market;

import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.domain.stock.StockValueRetriever;
import java.time.LocalDateTime;

public interface MarketState {
  MarketState update(Market market, LocalDateTime currentTime, StockValueRetriever stockValueRetriever);
}

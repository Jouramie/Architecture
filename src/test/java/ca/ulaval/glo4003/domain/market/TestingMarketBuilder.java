package ca.ulaval.glo4003.domain.market;

import ca.ulaval.glo4003.domain.market.states.ClosedMarketState;
import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockHistory;
import java.time.LocalTime;
import java.util.Collections;

public class TestingMarketBuilder {

  public Market build() {
    return new Market(
        new MarketId("market"),
        LocalTime.of(14, 30, 0),
        LocalTime.of(21, 0, 0),
        Currency.USD,
        Collections.singletonList(new Stock("stock", "name", "category", new MarketId("market"), new StockHistory())),
        new ClosedMarketState()
    );
  }
}

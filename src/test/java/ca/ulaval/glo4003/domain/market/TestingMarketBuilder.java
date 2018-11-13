package ca.ulaval.glo4003.domain.market;

import ca.ulaval.glo4003.domain.market.states.ClosedMarketState;
import ca.ulaval.glo4003.domain.market.states.Market;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockHistory;
import ca.ulaval.glo4003.domain.stock.StockValue;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class TestingMarketBuilder {

  private MarketState initialState = new ClosedMarketState();
  private MarketId marketId = new MarketId("market");
  private List<Stock> stocks;

  public TestingMarketBuilder() {
    StockHistory valueHistory = new StockHistory();
    valueHistory.addValue(LocalDate.now(), new StockValue(new MoneyAmount(1)));
    stocks = Collections.singletonList(new Stock("stock", "name", "category", new MarketId("market"), valueHistory));
  }

  public TestingMarketBuilder withId(MarketId id) {
    marketId = id;
    return this;
  }

  public TestingMarketBuilder withState(MarketState marketState) {
    initialState = marketState;
    return this;
  }

  public TestingMarketBuilder withStocks(List<Stock> stocks) {
    this.stocks = stocks;
    return this;
  }

  public Market build() {
    return new Market(
        marketId,
        LocalTime.of(14, 30, 0),
        LocalTime.of(21, 0, 0),
        Currency.USD,
        stocks,
        initialState
    );
  }
}

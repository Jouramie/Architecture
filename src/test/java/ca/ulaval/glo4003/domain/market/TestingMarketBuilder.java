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

  public static final String DEFAULT_STOCK_TITLE = "stock";
  private static final LocalTime DEFAULT_CLOSING_TIME = LocalTime.of(21, 0, 0);
  private static final LocalTime DEFAULT_OPENING_TIME = LocalTime.of(14, 30, 0);
  private static final String DEFAULT_STOCK_NAME = "name";
  private static final String DEFAULT_STOCK_CATEGORY = "category";
  private static final String DEFAULT_MARKET_NAME = "market";
  private boolean halted = false;
  private MarketState initialState = new ClosedMarketState();
  private MarketId marketId = new MarketId("market");
  private List<Stock> stocks;
  private String haltMessage = "";

  public TestingMarketBuilder() {
    StockHistory valueHistory = new StockHistory();
    valueHistory.addValue(LocalDate.now(), StockValue.create(new MoneyAmount(1)));
    stocks = Collections.singletonList(new Stock(DEFAULT_STOCK_TITLE,
        DEFAULT_STOCK_NAME, DEFAULT_STOCK_CATEGORY, new MarketId(DEFAULT_MARKET_NAME), valueHistory));
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

  public TestingMarketBuilder halted(String message) {
    halted = true;
    haltMessage = message;
    return this;
  }


  public Market build() {
    Market market = new Market(
        marketId,
        DEFAULT_OPENING_TIME,
        DEFAULT_CLOSING_TIME,
        Currency.USD,
        stocks,
        initialState
    );
    if (halted) {
      market.halt(haltMessage);
    }

    return market;
  }
}

package ca.ulaval.glo4003.util;

import ca.ulaval.glo4003.domain.market.MarketId;
import ca.ulaval.glo4003.domain.money.Currency;
import ca.ulaval.glo4003.domain.money.MoneyAmount;
import ca.ulaval.glo4003.domain.stock.Stock;
import ca.ulaval.glo4003.domain.stock.StockValue;
import ca.ulaval.glo4003.domain.stock.StockValueHistory;
import ca.ulaval.glo4003.service.stock.StockDto;
import java.time.LocalDate;

public class TestStockBuilder {
  public static final String DEFAULT_TITLE = "MSFT";
  public static final String DEFAULT_NAME = "Microsoft";
  public static final String DEFAULT_CATEGORY = "Tech";
  public static final MarketId DEFAULT_MARKET_ID = new MarketId("NASDAQ");
  public static final Currency DEFAULT_CURRENCY = Currency.USD;
  public static final MoneyAmount DEFAULT_OPEN_VALUE = new MoneyAmount(12.34, DEFAULT_CURRENCY);
  public static final MoneyAmount DEFAULT_CLOSE_VALUE = new MoneyAmount(56.78, DEFAULT_CURRENCY);

  private String title = DEFAULT_TITLE;
  private String name = DEFAULT_NAME;
  private String category = DEFAULT_CATEGORY;
  private MarketId marketId = DEFAULT_MARKET_ID;
  private MoneyAmount openValue = DEFAULT_OPEN_VALUE;
  private MoneyAmount closeValue = DEFAULT_CLOSE_VALUE;

  public TestStockBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public TestStockBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TestStockBuilder withCategory(String category) {
    this.category = category;
    return this;
  }

  public TestStockBuilder withMarketId(MarketId marketId) {
    this.marketId = marketId;
    return this;
  }

  public TestStockBuilder withOpenValue(MoneyAmount openValue) {
    this.openValue = openValue;
    return this;
  }

  public TestStockBuilder withCloseValue(MoneyAmount closeValue) {
    this.closeValue = closeValue;
    return this;
  }

  public Stock build() {
    StockValueHistory history = new StockValueHistory();
    history.addValue(LocalDate.now(), new StockValue(openValue, closeValue, closeValue));
    return new Stock(title, name, category, marketId, history);
  }

  public StockDto buildDto() {
    return new StockDto(title, name, category, marketId.getValue(), openValue.toUsd(),
        closeValue.toUsd(), closeValue.toUsd());
  }
}
